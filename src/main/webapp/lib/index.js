async function addFinishedProject(project, projectID) {
    let prCard = `<div class="card shadow" id = "questionnaireCardFinished">
        <div class="card-img-top">
          <img src="images/corp-party.jpg" class="containerImage rounded-1">
        </div>
        <div class="projectCardGrid">
          <div class="cardTitle fw-bold">${project[0]}</div>
          <div class="cardPoints">
            <label class="form-label fw-semibold">+${project[6]}</label>
          </div>
          <div class="cardCategories" id="categories">
          </div>
          <div class="cardDescription">The proposed municipality project aims to foster collaboration and community engagement through a corporate meeting and barbeque dinner. The event brings together local businesses, civic leaders, and residents to discuss future plans, exchange ideas, and build relationships. The meeting will provide a platform for open dialogue, enabling stakeholders to contribute their perspectives and suggestions for the town's development. Following the productive discussion, attendees will gather for a delightful barbeque dinner, fostering a relaxed atmosphere conducive to networking and fostering community spirit. This project aims to enhance cooperation, create a sense of belonging, and pave the way for a prosperous and united municipality.</div>
          <div class="cardButton">
            <div class="d-grid gap-2">
              <button type="button" class="btn btn-outline-success" data-bs-toggle="modal" data-bs-target="#questionModalMainFinished">
                See results
              </button>
            </div>
          </div>
          <div class="cardVotes">
            <label class="form-label">1037 votes</label>
          </div>
          <div class="cardEndDate">
            <label class="form-label">Finished</label>
          </div>
        </div>

        <div class="modal fade" id="questionModalMainFinished${projectID}" data-bs-backdrop="static" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content">
              <div class="modal-header">
                <div class="projectTitle">
                  <label class="form-label display-6 fw-bold" style="font-size: 30px"${project[0]}</label>
                </div>
                <div class="closeButton">
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
              </div>
              <div class="modal-body">
                <label class="cardDescriptionMain form-label">${project[1]}</label>
                <br>
                <div id="projectPhotoCarouselQFinished" class="carousel slide" data-bs-ride="true">
                  <div class="carousel-inner">
                    <div class="carousel-item active">
                      <img src="images/corp-party.jpg" class="d-block w-100">
                    </div>
                    <div class="carousel-item">
                      <img src="images/corp-table.jpg" class="d-block w-100">
                    </div>
                    <div class="carousel-item">
                      <img src="images/corp-grill.webp" class="d-block w-100">
                    </div>
                  </div>
                  <button class="carousel-control-prev" type="button" data-bs-target="#projectPhotoCarouselQFinished" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Previous</span>
                  </button>
                  <button class="carousel-control-next" type="button" data-bs-target="#projectPhotoCarouselQFinished" data-bs-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Next</span>
                  </button>
                </div>
                <br>
                <div id="mapQFinished"></div>
                <script src="webjars/leaflet/1.8.0/leaflet.js"></script>
                <script>
                  const latQFinished = 52.22268197916828;
                  const lonQFinished = 6.8961616942282955;
                  var mapQFinished = L.map('mapQFinished', {scrollWheelZoom: false}).setView([latQFinished, lonQFinished], 16);

                  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19,
                    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
                  }).addTo(mapQFinished);

                  L.marker([latQFinished, lonQFinished]).addTo(mapQFinished);
                  
                  document.getElementById("questionModalMainFinished"+${projectID}).addEventListener('shown.bs.modal', () => {
                    mapQFinished.invalidateSize();
                    mapQFinished.setView([latQFinished, lonQFinished], 16);
                  });
                </script>
              </div>
              <div class="modal-footer justify-content-center">
                <button class="btn btn-success" data-bs-target="#questionModalQuestionsFinished" data-bs-toggle="modal">Go to questions</button>
              </div>
            </div>
          </div>
        </div>

        <div class="modal fade" id="questionModalQuestionsFinished" data-bs-backdrop="static" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content">
              <div class="modal-header">
                <label class="form-label display-6 fw-bold" style="font-size: 30px">Voting result</label>
              </div>
              <div class="modal-body" style="display: grid" id="allQuestions">
              </div>
              <div class="modal-footer justify-content-center">
                <button type="button" class="btn btn-voice" data-bs-dismiss="modal">Close</button>
              </div>
            </div>
          </div>
        </div>
      </div>`
    document.getElementById("finishedCardsGrid").insertAdjacentHTML('beforeend', prCard)

    let receivedQ = await getProjectResults(projectID)
    for (let i = 0; i < receivedQ.length; i++) {
        let qText = receivedQ["text"]
        let sText = `<label class="questionText">
                    ${qText}
                  </label><br>`
        let qAnswers = receivedQ["choice"]
        let sAns = ``
        for (let j = 0; j < qAnswers.length; j++) {
            sAns += `<label class="form-label" style="margin-bottom: 0; margin-top: 15px">${qAnswers["text"]}</label>
                    <div class="progress" role="progressbar" aria-label="Example 20px high" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100" style="height: 38px">
                      <div class="progress-bar" style="width: 69%">${qAnswers["count"]}</div>
                    </div>`
        }
        let smallBody = `<div class="d-grid gap-2 col-6 mx-auto" role="group">${sAns}
                  </div>`
        let body = `<div class="questionBody">${sText+smallBody}
                </div>`
        document.getElementById("allQuestions").insertAdjacentHTML('beforeend', body)
    }

}

async function loopProjects() {
    let projectIDs = await getAllProjects()
    let projects = ``
    for (let i = 0; i < projectIDs.length; i++) {
        let project = await getProjectDetails(projectIDs[i])
        if (project[7] === 1) {
            await addFinishedProject(project, projectIDs[i])
            continue
        }
        let categories = project[4].split("|")
        categories.pop()
        if (await resolveProjectType(project[8])) {
            projects +=
                `<div class="card shadow" id="card${parseInt(projectIDs[i])}">
<span class="projectTypeBadge badge rounded-pill text-bg-primary" style="margin-right: 5px">Citizen</span>
        <div class="card-img-top">
          <img src="images/sack-race.jpg" class="containerImage rounded-1">
        </div>
        <div class="projectCardGrid">
          <div class="cardTitle fw-bold">${project[0]}</div>
          <div class="cardPoints">
            <label class="form-label fw-semibold">+${project[6]}</label>
          </div>
          <div class="cardCategories" id="categoryTags${i+1}">
          </div>
          <div class="cardDescription">${project[1]}</div>
          <div class="cardButton">
            <div class="d-grid gap-2">
              <button type="button" class="btn btn-voice-outline" id="voteBtn${i}" data-bs-toggle="modal" data-bs-target="#updownModal">
                Vote
              </button>
            </div>
          </div>
          <div class="cardVotes">
            <label class="form-label">632 votes</label>
          </div>
          <div class="cardEndDate">
            <label class="form-label">${project[5]}</label>
          </div>
        </div>

        <div class="modal fade" id="updownModal${projectIDs[i]}" data-bs-backdrop="static" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content">
              <div class="modal-header">
                <div class="projectTitle">
                  <label class="form-label display-6 fw-bold" style="font-size: 30px" id="qT${parseInt(project[8][0]["questionID"])}">
                    ${project[0]}
                  </label>
                </div>
                <div class="closeButton">
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
              </div>
              <div class="modal-body">
                <label class="form-label">
                  ${project[1]}
                </label>
                <br>
                <div id="projectPhotoCarousel1" class="carousel slide" data-bs-ride="true">
                  <div class="carousel-inner">
                    <div class="carousel-item active">
                      <img src="images/park-view.jpg" class="d-block w-100">
                    </div>
                    <div class="carousel-item active">
                      <img src="images/sack-race.jpg" class="d-block w-100">
                    </div>
                    <div class="carousel-item">
                      <img src="images/pull-activity.jpg" class="d-block w-100">
                    </div>
                    <div class="carousel-item">
                      <img src="images/picnic-area.jpg" class="d-block w-100">
                    </div>
                  </div>
                  <button class="carousel-control-prev" type="button" data-bs-target="#projectPhotoCarousel1" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Previous</span>
                  </button>
                  <button class="carousel-control-next" type="button" data-bs-target="#projectPhotoCarousel1" data-bs-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Next</span>
                  </button>
                </div>
                <br>
                <div id="mapUD"></div>
                <script src="webjars/leaflet/1.8.0/leaflet.js"></script>
                <script>
                  const latUD = 52.219058959737566;
                  const lonUD = 6.877778299326129;
                  var mapUD = L.map('mapUD').setView([latUD, lonUD], 16);

                  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19,
                    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
                  }).addTo(mapUD);

                  L.marker([latUD, lonUD]).addTo(mapUD);
                  document.getElementById("updownModal"+${projectIDs[i]}).addEventListener('shown.bs.modal', () => {
                      mapUD.invalidateSize();
                      mapUD.setView([latUD, lonUD], 16);
                  })
                </script>
              </div>
              <div class="modal-footer">
                <div class="modalUpdownButtons">
                  <div class="upvoteButton">
                    <button type="button" class="btn btn-success" style="--bs-btn-bg: #A4D0A4" data-bs-toggle="modal" data-bs-target="#responseSubmitted" onclick="getChosenAnswer(true, ${parseInt(project[8][0]["questionID"])}, this, ${project[0]})">Support</button>
                  </div>
                  <div class="downvoteButton">
                    <button type="button" class="btn btn-danger" style="--bs-btn-bg: #FF6D60" data-bs-toggle="modal" data-bs-target="#responseSubmitted" onclick="getChosenAnswer(true, ${parseInt(project[8][0]["questionID"])}, this, ${project[0]})">Oppose</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>`
        } else {
            projects += `<div class="card shadow" id ="card${parseInt(projectIDs[i])}">
        <span class="projectTypeBadge badge rounded-pill text-bg-primary" style="margin-right: 5px">Municipality</span>
        <div class="card-img-top">
          <img src="images/corp-party.jpg" class="containerImage rounded-1">
        </div>
        <div class="projectCardGrid">
          <div class="cardTitle fw-bold">${project[0]}</div>
          <div class="cardPoints">
            <label class="form-label fw-semibold">+${project[6]}</label>
          </div>
          <div class="cardCategories" id="categoryTags${i+1}">
          </div>
          <div class="cardDescription">${project[1]}</div>
          <div class="cardButton">
            <div class="d-grid gap-2">
              <button type="button" class="btn btn-voice-outline" id="voteBtn${i}" data-bs-toggle="modal" data-bs-target="#questionModalMain${projectIDs[i]}">
                Vote
              </button>
            </div>
          </div>
          <div class="cardVotes">
            <label class="form-label">1999 votes</label>
          </div>
          <div class="cardEndDate">
            <label class="form-label">${project[5]}</label>
          </div>
        </div>

        <div class="modal fade" id="questionModalMain${projectIDs[i]}" data-bs-backdrop="static" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content">
              <div class="modal-header">
                <div class="projectTitle">
                  <label class="form-label display-6 fw-bold" style="font-size: 30px">${project[0]}</label>
                </div>
                <div class="closeButton">
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
              </div>
              <div class="modal-body">
                <div class="cardDescription">${project[1]}</div>
                <br>
                <div id="projectPhotoCarousel2" class="carousel slide" data-bs-ride="true">
                  <div class="carousel-inner">
                    <div class="carousel-item active">
                      <img src="images/corp-party.jpg" class="d-block w-100">
                    </div>
                    <div class="carousel-item">
                      <img src="images/corp-table.jpg" class="d-block w-100">
                    </div>
                    <div class="carousel-item">
                      <img src="images/corp-grill.webp" class="d-block w-100">
                    </div>
                  </div>
                  <button class="carousel-control-prev" type="button" data-bs-target="#projectPhotoCarousel2" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Previous</span>
                  </button>
                  <button class="carousel-control-next" type="button" data-bs-target="#projectPhotoCarousel2" data-bs-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Next</span>
                  </button>
                </div>
                <br>
                <div id="mapQ"></div>
                <script src="webjars/leaflet/1.8.0/leaflet.js"></script>
                <script>
                  const latQ = 52.22268197916828;
                  const lonQ = 6.8961616942282955;
                  var mapQ = L.map('mapQ').setView([latQ, lonQ], 16);

                  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19,
                    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
                  }).addTo(mapQ);

                  L.marker([latQ, lonQ]).addTo(mapQ);
                  
                  document.getElementById("questionModalMain"+${projectIDs[i]}).addEventListener('shown.bs.modal', () => {
                    mapQ.invalidateSize();
                    mapQ.setView([latQ, lonQ], 16);
                  });
                </script>
              </div>
              <div class="modal-footer">
                <button class="btn btn-voice" data-bs-target="#questionModal${parseInt(project[8][0]["questionID"])*parseInt(project[8][0]["questionID"])+1}" data-bs-toggle="modal">Go to questions</button>
              </div>
            </div>
          </div>
        </div>
        <div id="questions${parseInt(projectIDs[i])}">
        </div>
      </div>`
            $("#voteBtn"+i).attr("disabled", project[9])
        }
        let cat = ``
        for (let j = 0; j < categories.length; j++) {
            cat += `<span class="badge rounded-pill text-bg-primary" style="margin-right: 5px">${categories[j]}</span>`
        }
        $("#categoryTags"+(i+1)).html(cat)
    }
    projects += `<div class="modal fade" id="responseSubmitted" data-bs-backdrop="static" aria-hidden="true">
        <div class="modal-dialog modal-sm modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <label class="questionText display-6" style="place-self:center; font-size: 25px">Your response has been submitted!</label>
            </div>
            <div class="modal-footer justify-content-center">
              <button type="button" class="btn btn-voice" data-bs-dismiss="modal">Close</button>
            </div>
          </div>
        </div>
      </div>`
    $("#ongoingCardsGrid").html(projects)
    for (let i = 0; i < projectIDs.length; i++) {
        let project = await getProjectDetails(projectIDs[i])
        await generateQuestions(project[8], projectIDs[i])
    }
}

async function resolveProjectType(questions) {
    if (questions.length === 1) {
        //let question = JSON.parse(questions[0])
        let question = questions[0]
        return question["title"] === "Do you support this project?";
    } else {
        return false
    }
    // if true, then project is type of Support/Oppose,
    // otherwise multiple choice
}

async function generateQuestions(questions, projectID) {
    //alert("inside modal generation")
    let questionModals = ``
    for (let i = 0; i < questions.length; i++) {
        //let question = JSON.parse(questions[i])
        let question = questions[i]
        let questionID = question["questionID"]
        let qID = questionID*questionID+i+1
        questionModals +=
            `<div class="modal fade" id="questionModal${parseInt(questionID)*parseInt(questionID)+i+1}" data-bs-backdrop="static" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content">
              <div class="modal-header">
                <label class="form-label display-6 fw-bold" style="font-size: 30px">Question ${i+1}</label>
              </div>
              <div class="modal-body" style="display: grid">
                <label class="questionText${(i+1)*10+1}" id="qT${parseInt(questionID)}">
                  ${question["title"]}
                </label>
                <br><br>
                <div class="d-grid gap-2 col-6 mx-auto" role="group" id="choices${parseInt(questionID)}">               
                </div>
              </div>
              <div class="modal-footer" id="footer${parseInt(questionID)}">
              </div>
            </div>
          </div>
        </div>`
    }
    document.getElementById("card"+projectID).insertAdjacentHTML('beforeend', questionModals)

    for (let i = 0; i < questions.length; i++) {
        let question = questions[i]
        let questionID = question["questionID"]
        let answers = question["answers"].split("|")
        answers.pop()
        let button = ``

        if (i+2 <= questions.length) {
            let question2 = questions[i+1]
            let questionID2 = question2["questionID"]
            // there is another question
            button = `<button class="btn btn-voice" data-bs-target="#questionModal${parseInt(questionID2)*parseInt(questionID2)+i+2}" data-bs-toggle="modal" onclick="getChosenAnswer(false, ${parseInt(questionID)}, ${parseInt(projectID)})">Next question</button>`
        } else {
            button = `<button class="btn btn-voice" data-bs-target="#responseSubmitted" data-bs-toggle="modal" onclick="getChosenAnswer(true, ${parseInt(questionID)}, ${parseInt(projectID)})">Submit response</button>`
        }

        let id = i+1
        $("#footer"+parseInt(questionID)).html(button)
        let ans = ``
        for (let j = 0; j < answers.length; j++) {
            ans += `<input type="radio" class="btn-check" name="vbtn-radio" id="choice${parseInt(questionID)*2+j+1}" autocomplete="off">
                  <label class="btn btn-voice-outline" for="choice${parseInt(questionID)*2+j+1}">${answers[j]}</label>`
        }
        $("#choices"+questionID).html(ans)
        await generateEventListeners(questions)
    }
}

async function generateEventListeners(questions) {
    for (let j = 0; j < questions.length; j++) {
        let question = questions[j]
        let questionID = question["questionID"]
        let answers = question["answers"].split("|")
        answers.pop()

        let choices = []
        let jd = parseInt(questionID)*parseInt(questionID) + j + 1
        document.getElementById("questionModal"+jd).addEventListener("submit", function (event) {
            event.preventDefault()
            for (let k = 0; k < answers.length; k++) {
                choices.push($("#choice"+questionID*2+k+1))
            }
            choices.forEach(element => {
                if (element.checked) {
                    $("#questionModal"+jd)._hideModal()

                    if (j+1 < questions.length) {
                        let nextQuestion = questions[j+1]
                        let questionID2 = nextQuestion["questionID"]
                        let jdd = parseInt(questionID2)*parseInt(questionID2)+j+1
                        $("#questionModal"+jdd).showModal()
                    } else {
                        $("#responseSubmitted").showModal()
                    }
                }
            })
        })
    }
}