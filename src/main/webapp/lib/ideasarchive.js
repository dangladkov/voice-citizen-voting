async function showDiv(divId) {
    document.getElementById("reviewIdeas").style.display = "none";
    document.getElementById("failedIdeas").style.display = "none";
    document.getElementById("approvedIdeas").style.display = "none";
    let a = document.getElementById("selectForm")
    if (a.options[a.selectedIndex].text !== "-Ideas Archive-") {
        await startIdeasLoad(a.selectedIndex - 1)
        document.getElementById(divId.value).style.display = "grid";
    }
}

async function startIdeasLoad(ideaType) {
    await loopIdeas(ideaType)
}

function test() {
    let a = document.getElementById("selectForm")
    console.log("value " + a.value)
    console.log("text " + a.options[a.selectedIndex].text)
}

async function getAllIdeasIDs(ideaType) {
    let response = "";
    return new Promise((resolve) => {
        $.ajax({
            type: 'GET',
            url: 'api/ideas/fetch/'+ideaType,
            cookie: cookiesConcat(),
            statusCode: {
                200: function () {
                    //console.log("idea received")

                },
                401: function () {
                    alert("User, you are Unauthorized. You will be redirected to login")
                    window.location.href = "login.html"
                },
                500: function (xhr) {
                    //alert("Something went wrong")
                    alert("Oops! " + xhr.responseText)
                    //alert(data['response'])
                    //window.location.href = "index.html"
                }
            },
            success: function (data) {

                const ids = data['ideasID']
                //console.log("ids " + ids)
                
                resolve([ids])
            },
            contentType: 'application/json',
            dataType: 'json'
        }).fail(function(data, textStatus, xhr) {
            errorFilter(xhr, data)
        })
    })
}

async function getIdea(IdeaID) {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'GET',
            url: 'api/ideas/' + IdeaID,
            cookie: cookiesConcat(),
            statusCode: {
                200: function () {


                },
                401: function () {
                    alert("User is unauthorized");
                    window.location.href = "login.html";
                    reject("User is unauthorized"); // Reject the promise on unauthorized access
                },
                500: function () {
                    alert("Something went wrong");

                    reject("Something went wrong"); // Reject the promise on error
                }
            },
            success: function (data) {
                let title = data['title'];
                let description = data['description'];
                let image = data['image'];
                let location = data['location'];
                let date = data['date'];
                let proposerUserID = data['proposerUserID'];

                let ideaStatus = data["ideaStatus"]
                let ideaID = data["ideaID"]

                resolve([title, description, ideaID, location, date, proposerUserID, ideaStatus]); // Resolve the promise with idea data


            },
            contentType: 'application/json',
            dataType: 'json'
        });
    });
}

async function archiveIdea(ideaID, ideaType) {
    $.ajax({
        type: 'POST',
        url: 'api/ideas/'+ideaID+'/archive/'+ideaType,
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {

            },
            401: function () {
                alert("User is unauthorized")
                window.location.href = "login.html"
            },
            403: function () {
                alert("Forbidden action")
                window.location.href = "index.html"
            },
            500: function () {
                alert("Something went wrong")
                window.location.href = "index.html"
            }
        },
        success: function () {
            
        },
        contentType: 'application/json',
        dataType: 'json'
    })
}

async function loopIdeas(ideaType) {
    let data = await getAllIdeasIDs(ideaType)
    console.log("data"+data[0].length)
    let ideas = ``
    for (let i = 0; i < data[0].length; i++) {
        let idea = await getIdea(data[0][i])
        ideas += `<div class="card shadow">
        <div class="card-img-top">
          <img src="images/sack-race.jpg" class="containerImage rounded-1">
        </div>
        <div class="projectCardGrid">
          <div class="cardTitle">
            <label class="form-label fw-bold">${idea[0]}</label>
          </div>
          <div class="cardPoints">
            <label class="form-label fw-semibold">+50</label>
          </div>
          <div class="cardCategories">
            <span class="badge rounded-pill text-bg-primary" id="categoryTag" style="margin-right: 5px">Social</span>
            <span class="badge rounded-pill text-bg-primary" style="margin-right: 5px">Environment</span>
          </div>
          <div class="cardDescription">${idea[1]}</div>
          <div class="cardButton">
            <div class="d-grid gap-2">
              <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#updownModal">
                Vote
              </button>
            </div>
          </div>
          <div class="cardVotes">
            <label class="form-label">632 votes</label>
          </div>
          <div class="cardEndDate">
            <label class="form-label">01/01/2024</label>
          </div>
        </div>

        <div class="modal fade" id="updownModal" data-bs-backdrop="static" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content">
              <div class="modal-header">
                <div class="projectTitle">
                  <label class="form-label display-6 fw-bold" style="font-size: 30px">${idea[0]}</label>
                </div>
                <div class="closeButton">
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
              </div>
              <div class="modal-body">
                <label class="form-label">
                  ${idea[1]}
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
                </script>
              </div>
              <div class="modal-footer">
                <div class="modalUpdownButtons">
                  <div class="upvoteButton">
                    <button type="button" class="btn btn-success" style="--bs-btn-bg: #A4D0A4" data-bs-toggle="modal" data-bs-target="#responseSubmitted">Support</button>
                  </div>
                  <div class="downvoteButton">
                    <button type="button" class="btn btn-danger" style="--bs-btn-bg: #FF6D60" data-bs-toggle="modal" data-bs-target="#responseSubmitted">Oppose</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>`
    }
    if (ideaType === 0) {
        $("#reviewIdeas").html(ideas)
    } else if (ideaType === 1) {
        $("#failedIdeas").html(ideas)
    } else if (ideaType === 2) {
        $("#approvedIdeas").html(ideas)
    }
}

async function getAllIdeasIDsNoIdeaTypeForOverviewProjectIdea() { //Also call iideasfetchall
    let response = "";
    return new Promise((resolve) => {
        $.ajax({
            type: 'GET',
            url: 'api/ideas/fetch-all',
            cookie: cookiesConcat(),
            statusCode: {
                200: function () {


                },
                401: function () {
                    alert("User, you are Unauthorized. You will be redirected to login")
                    window.location.href = "login.html"
                },
                500: function (xhr) {

                    alert("Oops! " + xhr.responseText)

                }
            },
            success: function (data) {

                const ids = data['ideasID']
                //console.log("ids " + ids)
                
                resolve(ids)
            },
            contentType: 'application/json',
            dataType: 'json'
        })
    })
}

async function getIdeasDetailsForProjectTransformation(ideaID){
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'GET',
            url: 'api/ideas/' + ideaID,
            cookie: cookiesConcat(),
            statusCode: {
                200: function () {
                    // console.log("idea received")

                },
                401: function () {
                    alert("User is unauthorized");
                    window.location.href = "login.html";
                    reject("User is unauthorized"); // Reject the promise on unauthorized access
                },
                500: function () {
                    alert("Something went wrong");

                    reject("Something went wrong"); // Reject the promise on error
                }
            },
            success: function (data) {
                let title = data['title'];
                let description = data['description'];
                let image = data['image'];
                let location = data['location'];
                let date = data['date'];
                let proposerUserID = data['proposerUserID'];
                let ideaStatus = data["ideaStatus"]


                $('#headingLabel').html("Transform the chosen Idea to a Project!");
                $('#titleLabel').val(title)
                $('#descriptionLabel').val(description)
                $('#locationInput').val(location)

                $('#dateLabel').val(date)



                resolve([title, description, image, location, date, proposerUserID, ideaStatus]); // Resolve the promise with idea data

            },
            contentType: 'application/json',
            dataType: 'json'
        });
    });
}