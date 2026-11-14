// async function showDiv(divId) {
//     document.getElementById("currentVotingProjects").style.display = "none";
//     document.getElementById("failedProjects").style.display = "none";
//     document.getElementById("successfulProjects").style.display = "none";
//     let a = document.getElementById("selectForm")
//     if (a.options[a.selectedIndex].text !== "-Projects Archive-") {
//         console.log(a.selectedIndex - 1)
//         await startProjectsLoad(a.selectedIndex - 1)
//         document.getElementById(divId.value).style.display = "block";
//     }
// }
//
// async function startProjectsLoad(projectType) {
//     await loopProjects(projectType)
// }
async function loopProjects() {
    let data = await getAllProjects()
    console.log("data"+data[0].length)
    let projects = ``
    for (let i = 0; i < data[0].length; i++) {
        let project = await getProjectDetails(data[0][i])
        projects += `<div class="card shadow">
        <div class="card-img-top">
          <img src="images/sack-race.jpg" class="containerImage rounded-1">
        </div>
        <div class="projectCardGrid">
          <div class="cardTitle">
            <label class="form-label fw-bold">${project[0]}</label>
          </div>
          <div class="cardPoints">
            <label class="form-label fw-semibold">+50</label>
          </div>
          <div class="cardCategories">
            <span class="badge rounded-pill text-bg-primary" id="categoryTag" style="margin-right: 5px">Social</span>
            <span class="badge rounded-pill text-bg-primary" style="margin-right: 5px">Environment</span>
          </div>
          <div class="cardDescription">${project[1]}</div>
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
                  <label class="form-label display-6 fw-bold" style="font-size: 30px">${project[0]}</label>
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
    $("#currentVotingProjects").html(projects)

}