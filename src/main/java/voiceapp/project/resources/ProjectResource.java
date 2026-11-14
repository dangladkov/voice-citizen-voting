package voiceapp.project.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import voiceapp.auth.dao.UserDao;
import voiceapp.auth.model.AuthData;
import voiceapp.db.DateParser;
import voiceapp.exceptions.InternalErrorException;
import voiceapp.exceptions.UnauthorizedException;
import voiceapp.idea.dao.IdeaDao;
import voiceapp.idea.model.IdeaArchiveType;
import voiceapp.project.dao.ProjectDao;
import voiceapp.project.dao.QuestionDao;
import voiceapp.project.dao.VoteDao;
import voiceapp.project.messages.create.CreateProjectMsg;
import voiceapp.project.messages.create.CreateQuestionChoiceMsg;
import voiceapp.project.messages.create.CreateQuestionMsg;
import voiceapp.project.messages.fetch.CompactProjectMsg;
import voiceapp.project.messages.fetch.ProjectFetchMsg;
import voiceapp.project.messages.results.VoteResultChoiceMsg;
import voiceapp.project.messages.results.VoteResultMsg;
import voiceapp.project.messages.results.VoteResultQuestionMsg;
import voiceapp.project.messages.vote.CastVoteMsg;
import voiceapp.project.messages.vote.VoteChoiceMsg;
import voiceapp.project.model.Project;
import voiceapp.project.model.Question;
import voiceapp.shop.dao.TransactionDao;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class containing all Jersey APIs for Project.
 */
@Path("project")
public class ProjectResource {
    /**
     * Function responsible for casting votes.
     * @param authData      Cookie authentication data containing UID and token.
     * @param projectID     ProjectID to cast vote on.
     * @param questions     Questions including choices.
     * @return
     */
    @Path("{projectID}/cast")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response castProjectVote(@CookieParam("Authentication") String authData, @PathParam("projectID") int projectID, CastVoteMsg questions) {
        AuthData ad = new AuthData(authData);

        if (!UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }
        Project projectToVote = ProjectDao.instance.getProject(projectID);

        // Make sure they haven't voted beforehand.
        for (Question q : Objects.requireNonNull(projectToVote).getQuestions()) {
            if (VoteDao.instance.hasVoted(ad.getUserID(), q.getQuestionID())) {
                // Has voted before.
                throw new NotAllowedException("You have already voted!");
            }

            // Verify that such choices exist and that the user is not submitting garbage.
            for (VoteChoiceMsg vcm : questions.getAnswers()) {
                // Submitted a choice which does not exist in the question (not matching ID and choice string).
                if (q.getQuestionID() == vcm.getVotingQuestionID() && !q.getAnswers().contains(vcm.getChoice())) {
                    throw new NotAllowedException("Invalid answer data!");
                }
            }
        }

        for (VoteChoiceMsg choice : questions.getAnswers()) {
            VoteDao.instance.vote(ad.getUserID(), choice.getVotingQuestionID(), choice.getChoice());
        }

        return Response.ok().build();
    }

    /**
     * Function responsible for submitting a project.
     * @param authData  Cookie authentication data containing UID and token.
     * @param cpm       Data message necessary for creating a project.
     * @return
     */
    @POST
    @Path("submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Integer submitProject(@CookieParam("Authentication") String authData, CreateProjectMsg cpm) {
        AuthData ad = new AuthData(authData);

        if (!UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        if (!UserDao.instance.isAdmin(ad.getUserID())) {
            throw new ForbiddenException();
        }

        int projectID;

        if (cpm.getIdeaID() == -1) {
            projectID = ProjectDao.instance.addProject(cpm.getTitle(), cpm.getDescription(), cpm.getLocation(), ad.getUserID(), DateParser.getDate(cpm.getVotingDeadline()), cpm.getPointsToAward(), cpm.getCategories());
        } else {
            projectID = IdeaDao.instance.convertToProject(cpm.getIdeaID());
            ProjectDao.instance.addCategories(projectID, cpm.getCategories());
        }

        for (CreateQuestionMsg cqm : cpm.getQuestions()) {
            int questionID = QuestionDao.instance.addQuestion(projectID, cqm.getText());
            System.out.println("Added question " + questionID + " with text " + cpm.getTitle());
            if (questionID == -1) {
                throw new InternalErrorException("Failed to create question! Please report! SP01");
            }

            StringBuilder choices = new StringBuilder();
            for (CreateQuestionChoiceMsg choice : cqm.getChoices()) {
                System.out.println("Added choice " + choice.getText());
                choices.append(choice.getText() + "|");
                //QuestionDao.instance.addAnswer(questionID, choice.getText());
            }

            QuestionDao.instance.addAnswer(questionID, choices.toString());
        }

        Project project = ProjectDao.instance.getProject(projectID);

        if (project == null) {
            throw new InternalErrorException("Failed to locate project after creation! Please report! SP02");
        }

        boolean result = TransactionDao.instance.sendPoints(project.getProposedBy(), ProjectDao.PROJECT_POINTS);

        if (!result) {
            throw new InternalErrorException("Failed to give points to the user that proposed the idea! SP03");
        }

        return projectID;
    }

    /**
     * Function used for getting a specific project.
     * @param authData      Cookie authentication data containing UID and token.
     * @param projectID     Project to retrieve.
     * @return              Message containing all details about this specific project.
     */
    @GET
    @Path("{projectID}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectFetchMsg getProject(@CookieParam("Authentication") String authData, @PathParam("projectID") int projectID) {
        AuthData ad = new AuthData(authData);

        if (!UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        // Refresh project statuses
        List<Project> projects = ProjectDao.instance.getProjects();
        if (projects != null && projects.isEmpty()) {
            refreshProjectStates(projects);
        }

        Project project = ProjectDao.instance.getProject(projectID);
        System.out.println(project.getCategories());
        if (project == null) {
            throw new NotFoundException();
        }

        return new ProjectFetchMsg(project.getTitle(), project.getDescription(),
                project.getLocation(), project.getDate(), project.getCategories(), project.getDeadline(),
                project.getPointsToAward(), project.getProjectStatus(), VoteDao.instance.hasVotedOnProject(ad.getUserID(), projectID), project.getQuestions());
    }

    /**
     * Function which retrieves the results of a project.
     * @param authData      Cookie authentication data containing UID and token.
     * @param projectID     Project's ID (the one we're fetching for)
     * @return              Message containing all results of the project.
     */
    @GET
    @Path("{projectID}/results")
    @Produces(MediaType.APPLICATION_JSON)
    public VoteResultMsg getResults(@CookieParam("Authentication") String authData, @PathParam("projectID") int projectID) {
        AuthData ad = new AuthData(authData);

        if (!UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        Project project = ProjectDao.instance.getProject(projectID);

        if (project == null) {
            throw new NotFoundException();
        }

        List<VoteResultQuestionMsg> vrqm = new ArrayList<>();

        for (Question q : project.getQuestions()) {
            String[] choices = q.getAnswers().split(",");

            VoteResultQuestionMsg questionResultMsg = new VoteResultQuestionMsg(q.getTitle());

            for (String s : choices) {
                int result = VoteDao.instance.getResult(q.getQuestionID(), projectID, s);
                VoteResultChoiceMsg vrcm = new VoteResultChoiceMsg(s, result);
                questionResultMsg.addChoice(vrcm);
            }
        }

        return new VoteResultMsg(vrqm);
    }

    /**
     * Function responsible for editing a project.
     * @param authData      Cookie authentication data containing UID and token.
     * @param projectID     Project's ID (the one we're going to edit)
     * @param cpm           Data necessary for editing the project.
     * @return              Result of the execution (status code)
     */
    @POST
    @Path("{projectID}/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response editProject(@CookieParam("Authentication") String authData, @PathParam("projectID") int projectID, CreateProjectMsg cpm) {
        AuthData ad = new AuthData(authData);

        if (!UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        if (!UserDao.instance.isAdmin(ad.getUserID())) {
            throw new ForbiddenException();
        }

        Timestamp deadline;

        try {
            deadline = new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(cpm.getDate()).getTime());
        } catch (ParseException e) {
            throw new InternalErrorException("Incorrect date time format! EP01");
        }

        boolean result = ProjectDao.instance.updateProject(projectID, cpm.getIdeaID(), cpm.getTitle(), cpm.getDescription(),
                cpm.getLocation(), deadline, cpm.getPointsToAward(), cpm.getCategories());
        if (result) {
            return Response.ok().build();
        } else {
            throw new InternalErrorException("Failed to update project! Please report! EP02");
        }
    }

    /**
     * Retrieves all project IDs that are available.
     * @param authData  Cookie authentication data containing UID and token.
     * @return          Message containing all available project IDs.
     */
    @GET
    @Path("fetch-all")
    @Produces(MediaType.APPLICATION_JSON)
    public CompactProjectMsg fetchAll(@CookieParam("Authentication") String authData) {
        AuthData ad = new AuthData(authData);

        if (!UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        List<Integer> projects = ProjectDao.instance.getProjectIDs();

        if (projects == null || projects.isEmpty()) {
            return null;
        }

        // Refresh project statuses
        List<Project> projectObjects = ProjectDao.instance.getProjects();
        if (projects != null && projects.isEmpty()) {
            refreshProjectStates(projectObjects);
        }

        return new CompactProjectMsg(projects);
    }

    /**
     * Deletes a project from the backend.
     * @param authData      Cookie authentication data containing UID and token.
     * @param projectID     Project ID to delete.
     * @return              Result of the execution (status code)
     */
    @DELETE
    @Path("{projectID}")
    public Response deleteProject(@CookieParam("Authentication") String authData, @PathParam("projectID") int projectID) {
        AuthData ad = new AuthData(authData);

        if (!UserDao.instance.isValidSession(ad.getUserID(), ad.getToken())) {
            throw new UnauthorizedException();
        }

        if (!UserDao.instance.isAdmin(ad.getUserID())) {
            throw new ForbiddenException();
        }

        boolean result = ProjectDao.instance.removeProject(projectID);

        if (result) {
            return Response.ok().build();
        } else {
            throw new InternalErrorException("Failed to delete project! Please report! DP01");
        }
    }

    /**
     * Refreshes the current projects.
     * If the deadline has passed, it marks it as "complete" and does not allow the users to vote further.
     * @param projects  Projects to refresh.
     */
    private void refreshProjectStates(List<Project> projects) {
        for (Project p : projects) {
            Timestamp current = new Timestamp(System.currentTimeMillis());
            Timestamp deadline = p.getDeadline();

            if (current.getTime() > deadline.getTime()) {
                // Current project has passed the deadline
                ProjectDao.instance.setStatus(p.getProjectID(), 1);
            }
        }
    }
}
