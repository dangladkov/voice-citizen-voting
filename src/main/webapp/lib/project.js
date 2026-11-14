async function castVote(projectID){
    let objQuestions = answersToCast

    $.ajax({
        type: 'POST',
        url: 'api/project/'+projectID+'/cast',
        data: JSON.stringify({answers: objQuestions}),
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {
                //alert("vote submitted")
                //window.location.href = "index.html"
                answersToCast = []
            },
            401: function () {
                alert("User, you are Unauthorized. You will be redirected to login");
                window.location.href = "login.html"
                answersToCast = []
            },

            500: function (xhr) {
                //alert("Oops! " + xhr.responseText)
                //window.location.href = "login.html"
                answersToCast = []
            }
        },
        success: function () {
        },
        contentType: "application/json",
        dataType: 'json'
    })
}

async function getProjectDetails(projectID){
    return new Promise((resolve) => {
        $.ajax({
            type: 'GET',
            url: 'api/project/' + projectID,
            cookie: cookiesConcat(),
            statusCode: {
                200: function () {
                },
                401: function () {
                    alert("User is unauthorized")
                    window.location.href = "login.html"
                    //reject("User is unauthorized"); // Reject the promise on unauthorized access
                },
                500: function (xhr) {
                    alert("Oops! " + xhr.responseText)
                    //window.location.href = "index.html";
                    //reject("Oops! " + xhr.responseText); // Reject the promise on error
                }
            },
            success: function (data) {
                let title = data['title'];
                let description = data['description'];
                let location = data['location'];
                let date = data['date'];
                let categories = data['categories']; //Array splitted by commas
                let votingDeadline = data['votingDeadline']
                let pointsToAward = data['pointsToAward']
                let projectStatus = data['projectStatus']
                let questions = data["questions"]
                let hasVoted = data["hasVoted"]
                resolve([title, description, location, date, categories, votingDeadline, pointsToAward, projectStatus, questions, hasVoted]); // Resolve the promise with project data
                ////new

            },
            contentType: 'application/json',
            dataType: 'json'
        });
    });
}

async function getProjectDetailsForEdit(projectID){
    return new Promise((resolve) => {
        $.ajax({
            type: 'GET',
            url: 'api/project/' + projectID,
            cookie: cookiesConcat(),
            statusCode: {
                200: function () {
                },
                401: function () {
                    alert("User is unauthorized")
                    window.location.href = "login.html"
                    //reject("User is unauthorized"); // Reject the promise on unauthorized access
                },
                500: function (xhr) {
                    alert("Oops! " + xhr.responseText)
                    //window.location.href = "index.html";
                    //reject("Oops! " + xhr.responseText); // Reject the promise on error
                }
            },
            success: function (data) {
                let title = data['title'];
                let description = data['description'];
                let location = data['location'];
                let date = data['date'];
                let categories = data['categories']; //Array splitted by commas
                let votingDeadline = data['votingDeadline']
                let pointsToAward = data['pointsToAward']
                let projectStatus = data['projectStatus']
                let questions = data["questions"]
                let hasVoted = data["hasVoted"]

                $('#headingLabel').html("Edit the chosen Project!");
                $('#titleLabel').val(title)
                $('#descriptionLabel').val(description)
                $('#locationInput').val(location)
                $('#pointsLabel').val(pointsToAward)

                $('#dateLabel').val(date)
                $('#dateVoteLabel').val(votingDeadline)

                //Missing date, VotingDeadline, categories, questions
                document.getElementById("submitButton").innerText = "Edit Project"

                $('#submitButton').attr('onclick', 'editProject('+projectID+');')
                for (let i = 0; i < categories.length; i++){
                    switch (categories[i]){
                        case ("Social"):
                            $('#socialCat').checked = true;
                            break;
                        case ("Education"):
                            $('#educationCat').checked = true;
                            break;
                        case ("Culture"):
                            $('#cultureCat').checked = true;
                            break;
                        case ("Corporate"):
                            $('#corporateCat').checked = true;
                            break;
                        case ("Community"):
                            $('#communityCat').checked = true;
                            break;
                        case ("Environment"):
                            $('#environmentCat').checked = true;
                            break;
                        case ("Tech"):
                            $('#techCat').checked = true;
                            break;
                        case ("Government"):
                            $('#governmentCat').checked = true;
                            break;
                        case ("Health"):
                            $('#healthCat').checked = true;
                            break;
                        case ("Leisure"):
                            $('#leisureCat').checked = true;
                            break;
                        case ("Sports"):
                            $('#sportsCat').checked = true;
                            break;
                        case ("Religion"):
                            $('#religionCat').checked = true;
                            break;
                        case ("Charity"):
                            $('#charityCat').checked = true;
                            break;
                        case ("Tourism"):
                            $('#tourismCat').checked = true;
                            break;
                    }
                }
                resolve([title, description, location, date, categories, votingDeadline, pointsToAward, projectStatus, questions, hasVoted])
                },

            contentType: 'application/json',
            dataType: 'json'
        });
    });
}

function changeIdeaStatus(ideaID){
    let data = getIdea(ideaID);
    console.log("Original Status: "+ data['ideaStatus'])
    data['ideaStatus'] = 1
    console.log("Changed Status: "+ data['ideaStatus'])
}

let answersToCast = []
async function getChosenAnswer(lastQuestion, questionID, projectID) {
    //let qText = $("#qT"+questionID).val()
    let inputs = $("#choices"+questionID).find("input")
    let aText
    inputs.each(function () {
        if (this.checked) {
            //aText = this.value
            let id = this.id
            let labels = $("#choices"+questionID).find("label")
            labels.each(function () {
                if (this.htmlFor === id) {
                    aText = this.textContent
                    return false
                }
            })
            return false
        }
    })
    let objQA = {votingQuestionID: questionID, choice: aText}
    answersToCast.push(objQA)
    if (lastQuestion) {
        await castVote(projectID)
    }
}

async function publishProject(ideaReferenceID){
    let queryString = window.location.search;
    let urlParams = new URLSearchParams(queryString);
    let passedVariable = parseInt(urlParams.get('var'));
    let data = await getIdeasDetailsForProjectTransformation(passedVariable)
    console.log("This is teh data to test: "+data)
    console.log(passedVariable)
    ideaReferenceID = passedVariable;

    let title = $('#titleLabel').val();
    let description = $('#descriptionLabel').val();
    let location = $('#locationInput').val();
    let date = $('#dateLabel').val();
    let votingDeadline = $('#dateVoteLabel').val();
    let pointsToAward = $('#pointsLabel').val();
    let category = "";

    $("#categories input:checkbox").each(function () {
        if (this.checked) {
            let cat = (this.id).replace("Cat", "")
            category += cat + "|"
        }
        category.slice(0, -1)
    })



    let objQuestions = []
    let objAnswers = []

    if (ideaReferenceID === -1) {
        for (let i = 1; i <= document.getElementById("questionCount").value; i++) {
            objAnswers = []
            let objQuestion = {text: $("#question"+i).val()}
            let items = document.getElementById("answerGroup"+i).getElementsByTagName("li")
            for (let j = 0; j < items.length; j++) {
                let id = i*10+j+1
                let ans = $("#answer"+id).val()
                let objAnswer = {text: ans}
                objAnswers.push(objAnswer)
            }
            Object.assign(objQuestion, {choices: objAnswers})
            objQuestions.push(objQuestion)
        }
    } else {
        let objQuestion = {text: "Do you support this project?"}
        objAnswers = [{text: "Support"},{text: "Oppose"}]
        Object.assign(objQuestion, {choices: objAnswers})
        objQuestions.push(objQuestion)
    }

    $.ajax({
        type: 'POST',
        url: 'api/project/submit',
        data: JSON.stringify({ideaID: ideaReferenceID, title: title, description: description, location: location,
            date: date, categories: category, votingDeadline: votingDeadline, pointsToAward: pointsToAward, questions: objQuestions}),
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {
                changeIdeaStatus(ideaReferenceID);
                alert("Project submitted")
                window.location.href = "index.html"
            },
            401: function () {
                alert("User, you are Unauthorized. You will be redirected to login");
                window.location.href = "login.html"
            },
            403: function () {
                alert("User, this action is forbidden. You have insufficient access level")
                //window.location.href = "login.html"
            },
            500: function (xhr) {
                alert("Oops! " + xhr.responseText)
                //window.location.href = "login.html"
            }
        },
        success: async function (data) {
            await uploadImage($("#imageUpload").prop('files'), "project", data)

        },
        contentType: "application/json",
        dataType: 'json'
    })//.done(function() {alert("Successful POST!");})
}

async function getAllProjects(){
    return new Promise((resolve) => {
        $.ajax({
            type: 'GET',
            url: 'api/project/fetch-all',
            cookie: cookiesConcat(),
            statusCode: {
                200: function () {
                },
                401: function () {
                    alert("User, you are Unauthorized. You will be redirected to login");
                    window.location.href = "login.html"
                },
                500: function (xhr) {
                    alert("Oops! " + xhr.responseText)
                    //window.location.href = "index.html"
                }
            },
            success: function (data) {
                const ids = data['projectIDs']
                resolve(ids)
            },
            contentType: 'application/json',
            dataType: 'json'
        })
    })
}

async function getProjectResults(projectID) {
    return new Promise((resolve) => {
        $.ajax({
            type: 'GET',
            url: 'api/project/'+projectID+'/results',
            cookie: cookiesConcat(),
            statusCode: {
                200: function () {
                },
                401: function () {
                    alert("User is unauthorized")
                    window.location.href = "login.html"
                },
                404: function () {
                    alert("Project not found")
                },
                500: function () {
                    alert("Something went wrong")
                }
            },
            success: function (data) {
                let questions = data["questions"]
                resolve(questions)
            },
            contentType: 'application/json',
            dataType: 'json'
        })
    })
}

async function editProject(projectID){
    let title = $('#titleLabel').val();
    let description = $('#descriptionLabel').val();
    let location = $('#locationInput').val();
    let categories = "";
    let date = $('#dateLabel').val();
    let votingDeadline = $('#dateVoteLabel').val();
    let pointsToAward = $('#pointsLabel').val();

    // $("#categories input:checkbox").each(function () {
    //     if ($(this).checked) {
    //         categories += $(this).val() + ","
    //     }
    // })
    $("#categories input:checkbox").each(function () {
        if (this.checked) {
            let cat = (this.id).replace("Cat", "")
            categories += cat + ","
        }
    })

    let objQuestions = []
    let objAnswers = []

    if (ideaReferenceID === -1) {
        for (let i = 1; i <= document.getElementById("questionCount").value; i++) {
            objAnswers = []
            let objQuestion = {text: $("#question"+i).val()}
            let items = document.getElementById("answerGroup"+i).getElementsByTagName("li")
            for (let j = 0; j < items.length; j++) {
                let id = i*10+j+1
                let ans = $("#answer"+id).val()
                let objAnswer = {text: ans}
                objAnswers.push(objAnswer)
            }
            Object.assign(objQuestion, {choices: objAnswers})
            objQuestions.push(objQuestion)
        }
    } else {
        let objQuestion = {text: "Do you support this project?"}
        objAnswers = [{text: "Support"},{text: "Oppose"}]
        Object.assign(objQuestion, {choices: objAnswers})
        objQuestions.push(objQuestion)
    }

    $.ajax({
        type: 'POST',
        url: 'api/project/'+projectID+'/edit',
        data: JSON.stringify({title: title, description: description, location: location,
            eventDate: date, categories: categories, votingDeadline: votingDeadline, pointsToAward: pointsToAward,
            questions: objQuestions}), //projectQuestion is a sub-json element. projectQuestion: text, projectChoice: choiceText
        // projectChoice is another (different) sub-json element.
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {
                alert("Project Edited!")
                window.location.href = "overviewprojectidea.js"
            },
            401: function () {
                alert("User, you are Unauthorized. You will be redirected to login");
                window.location.href = "login.html"
            },
            500: function (xhr) {
                alert("Oops! " + xhr.responseText)
                //window.location.href = "index.html"
            }
        },
        success: function () {
        },
        contentType: 'application/json',
        dataType: 'json'
    })
}

async function deleteProject(projectID){
    $.ajax({
        type: 'DELETE',
        url: 'api/project/'+projectID,
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {
                alert("Project Deleted!")
                window.location.href = "overviewprojectidea.html"
            },
            401: function () {
                alert("User, you are Unauthorized. You will be redirected to login");
                window.location.href = "login.html"
            },
            403: function (){
                alert("User, this action is forbidden. You have insufficient access level");
            },
            500: function (xhr) {
                alert("Oops! " + xhr.responseText)
                //window.location.href = "login.html"
            }
        },
        success: function () {
             //new
        },
        contentType: 'application/json',
        dataType: 'json'
    })
}

async function loopAllProjects(){
    let data = await getAllProjects()
    let projects = ``
    for (let i = 0; i < data.length; i++) {
        let project = await getProjectDetails(data[i])
        projects += ` <br>
        <table class="table" style="text-align: center">
            <thead>
            <tr>
                <th scope="col">Number</th>
                <th scope="col">Project</th>
                <th scope="col">Status</th>
                <th scope="col">Modify</th>
            </tr>
            </thead>
            <tbody>
                <tr>
                <th scope="row">
                    <div class="list-group" style = "--bs-list-group-border-color: transparent; --bs-list-group-action-active-bg: transparent; --bs-list-group-active-color: transparent; --bs-list-group-action-hover-bg: transparent " >
                        <a class="list-group-item list-group-item-action" style="padding:0;"> ${(i + 1) + ". "} </a>
                    </div>
                </th>
                <td>
                    <div class="list-group" style = "--bs-list-group-border-color: transparent; --bs-list-group-action-active-bg: transparent; --bs-list-group-active-color: transparent; --bs-list-group-action-hover-bg: transparent " >
                        <a class="list-group-item list-group-item-action" style="padding:0;" href="projectdashboard.html"> ${project[0]}</a>
                    </div>
                </td>
                <td>
                    <div class="list-group" style = "--bs-list-group-border-color: transparent; --bs-list-group-action-active-bg: transparent; --bs-list-group-active-color: transparent; --bs-list-group-action-hover-bg: transparent " >
                        <a class="list-group-item list-group-item-action" style="padding:0;">${project[7]}</a>
                    </div>
                </td>
                <td>
                    <div class = "btn-group">
                        
                        <button type="button" class="btn btn-voice-primary edit-btn" id="EditButtonProjectIndex${i}" onclick="getProjectDetailsForEdit(${project[8][0]['projectID']});transitionToProjectInfo(${project[8][0]['projectID']})">Edit</button> 
                        <button type="button" class="btn btn-voice-danger delete-btn" id="DeleteButtonProjectIndex${i}" onclick="deleteProject(${project[8][0]['projectID']})">Delete</button>
                    </div>
                </td>
            </tr>`
    }
    $("#listList").html(projects)
}
function transitionToProjectInfo(projectID) {
    window.location.href = "submitproject.html?var=" + encodeURIComponent(projectID)
}

function transitionToIdeaInfo(ideaID) {
    window.location.href = "reviewidea.html?var=" + encodeURIComponent(ideaID)
}

async function loopAllIdeas(){
    let data = await getAllIdeasIDsNoIdeaTypeForOverviewProjectIdea()
    let ideas = ``
    for (let i = 0; i < data.length; i++) {
        let idea = await getIdea(data[i])
        ideas += `
 
                <tr>
                <th scope="row">
                    <div class="list-group" style = "--bs-list-group-border-color: transparent; --bs-list-group-action-active-bg: transparent; --bs-list-group-active-color: transparent; --bs-list-group-action-hover-bg: transparent " >
                        <a class="list-group-item list-group-item-action" onclick = getIdeasDetailsForProjectTransformation(${idea[2]});transitionToIdeaInfo(${idea[2]}) style="padding:0;"> ${(i + 1) + ". "} </a>
                    </div>
                </th>
                <td>
                    <div class="list-group" style = "--bs-list-group-border-color: transparent; --bs-list-group-action-active-bg: transparent; --bs-list-group-active-color: transparent; --bs-list-group-action-hover-bg: transparent " >
                        <a class="list-group-item list-group-item-action" onclick = getIdeasDetailsForProjectTransformation(${idea[2]});transitionToIdeaInfo(${idea[2]}) style="padding:0;"> ${idea[0]}</a>
                    </div>
                </td>
                <td>
                    <div class="list-group" style = "--bs-list-group-border-color: transparent; --bs-list-group-action-active-bg: transparent; --bs-list-group-active-color: transparent; --bs-list-group-action-hover-bg: transparent " >
                        <a class="list-group-item list-group-item-action" onclick = getIdeasDetailsForProjectTransformation(${idea[2]});transitionToIdeaInfo(${idea[2]}) style="padding:0;">${idea[6]}</a>
                    </div>
                </td> 
            </tr>`
    }
    $("#listListIdeas").html(ideas)
}