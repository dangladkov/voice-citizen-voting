async function addBenefit() {
    let title = $('#BenefitTitle').val()
    let description = $('#BenefitDescription').val()
    let points = $('#BenefitPoints').val()
    let a = document.getElementById("exampleFormControlSelect2")
    let category = a.options[a.selectedIndex].text
    //let images = 'img'
    let objArray = []
    for (let i = 1; i <= numberOfAttributes; i++) {
        if ($('#countBenefit' + i).val().length > 0) {
            let obj = {count: $('#countBenefit'+i).val()}
            if (document.getElementById("colorCheckbox"+i).checked) {
                Object.assign(obj, {color: parseInt($('#colorBenefit'+i).find(":selected").val())})
            } else {
                Object.assign(obj, {color: null})
            }
            if (document.getElementById("sizeCheckbox"+i).checked && $('#sizeBenefit'+i).val().length > 0) {
                Object.assign(obj, {size: $('#sizeBenefit'+i).val()})
            } else {
                Object.assign(obj, {size: null})
            }
            objArray.push(obj)
        }
    }
    numberOfAttributes = 1

    if (objArray.length > 0) {
        $.ajax({
            type: 'POST',
            url: 'api/shop/add',
            data: JSON.stringify({title: title, description: description, points: points, category: category, attributes: objArray}),
            cookie: cookiesConcat(),
            statusCode: {
                200: function () {
                    //window.location.href = "shopmanagement.html"
                    alert("Benefit Submitted!")
                    window.location.href = "shopmanagement.js"
                },
                401: function () {
                    alert("User, you are Unauthorized. You will be redirected to login");
                    window.location.href = "login.html"
                },
                403: function () {
                    alert("User, this action is forbidden. You have insufficient access level")
                    //window.location.href = "index.html"
                },
                500: function (xhr) {
                    alert("Oops! " + xhr.responseText)
                    //window.location.href = "index.html"
                }
            },
            success: async function (data) {
                await uploadImage($("#benefitImagesInput").prop('files'), "shop", data)
            },
            contentType: 'application/json',
            dataType: 'json'
        })
    } else {
        alert("Attributes need to be entered (at least count)")
    }
}

async function getBenefitDetails(benefitID){
    return new Promise((resolve, reject) => {
    $.ajax({
        type: 'GET',
        url: 'api/shop/'+benefitID,
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {

            },
            401: function () {
                alert("User, you are Unauthorized. You will be redirected to login");
                window.location.href = "login.html";
                reject("User, you are Unauthorized. You will be redirected to login");
            },
            500: function (xhr) {
                alert("Oops! " + xhr.responseText)
                //window.location.href = "index.html";
                reject("Oops! " + xhr.responseText);
            }
        },
        success: async function (data) {
            let benefitID = data["benefitID"];
            let benefitTitle = data["title"];
            let benefitDescription = data["description"];
            let benefitPoints = data["points"];
            let benefitCategory = data["categories"];
            let objectToParse = data["attributes"]
            let myObject = JSON.parse(objectToParse)
            let parsedObjs = JSON.parse(myObject)

            resolve([benefitID, benefitTitle, benefitDescription, benefitPoints, benefitCategory, parsedObjs])
        },
        contentType: 'application/json',
        dataType: 'json'
    });
    })
}
async function getBenefitDetailsForEdit(benefitID){
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'GET',
            url: 'api/shop/'+benefitID,
            cookie: cookiesConcat(),
            statusCode: {
                200: function () {
                },
                401: function () {
                    alert("User, you are Unauthorized. You will be redirected to login");
                    //window.location.href = "login.html";
                    reject("User, you are Unauthorized. You will be redirected to login");
                },
                500: function (xhr) {
                    alert("Oops! " + xhr.responseText)
                    //window.location.href = "index.html";
                    reject("Oops! " + xhr.responseText);
                }
            },
            success: async function (data) {
                let benefitID = data["benefitID"];
                let benefitTitle = data["title"];
                let benefitDescription = data["description"];
                let benefitPoints = data["points"];
                let benefitCategory = data["category"];
                // let benefitImages = data["images"];

                let objectToParse = data["attributes"]
                let myObject = JSON.parse(objectToParse)
                let parsedObjs = JSON.parse(myObject) // this is array of all attributes
                //parsedObjs[0]["count"] <- accesses param of the object

                if (parsedObjs.length > 1){
                    await appendAttributesForEdit(parsedObjs.length)
                }

                for (let i = 0; i < parsedObjs.length; i++) {
                    console.log("i="+i)
                    let obj = parsedObjs[i]//attribute
                    let benefitCount = obj["count"]
                    console.log("count "+benefitCount)
                    let benefitColor = obj["color"]
                    console.log("color "+benefitColor)
                    let benefitSize = obj["size"];
                    console.log("size "+benefitSize)

                    if (benefitCount !== null) {
                        $('#countBenefit'+(i+1)).val(benefitCount)
                    }
                    if (benefitColor !== null) {
                        $('#colorBenefit'+(i+1)).val(benefitColor)
                        document.getElementById('colorCheckbox'+(i+1)).checked = true
                    }
                    if (benefitSize !== null) {
                        $('#sizeBenefit'+(i+1)).val(benefitSize)
                        document.getElementById('sizeCheckbox'+(i+1)).checked = true
                    }

                }

                $('#PageTitle').html("Edit the chosen Item!");
                $('#BenefitTitle').val(benefitTitle)
                $('#BenefitDescription').val(benefitDescription)
                $('#BenefitPoints').val(benefitPoints)
                $('#BenefitCategory').val(benefitCategory)
                $('#exampleFormControlSelect2').val(benefitCategory) //get sub-json attributes
                // $('#countBenefit').val(benefitCount)
                // $('#colorCheckbox').val(benefitColor) //get sub-json attributes
                // console.log(benefitColor)
                // $('#sizeCheckbox').val(benefitSize) //get sub-json attributes
                document.getElementById("submitBenefit").innerText = "Edit benefit"
                //$('#submitBenefit').attr('onclick', alert("Success"))

                $('#submitBenefit').attr('onclick', 'editBenefit('+benefitID+');')
               //$('#submitBenefit').setAttribute('onclick', 'editBenefit('+benefitID+');')
                //document.getElementById("submitBenefit").setAttribute("onClick",'editBenefit('+benefitID+');')
                //$('#submitBenefit').attr('onclick', window.location.reload())

                //$('#submitBenefit').attr('onclick', alert("Successfully edited. Reload page"))
                //$('#submitBenefit').click((await editBenefit(benefitID)))

                resolve([benefitID, benefitTitle, benefitDescription, benefitPoints, benefitCategory]); // Resolve the promise with benefit data

            },
            contentType: 'application/json',
            dataType: 'json'
        });
    });
}

async function getAllBenefits(){
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'GET',
            url: 'api/shop/preview',
            cookie: cookiesConcat(),
            statusCode: {
                200: function () {
                },
                401: function () {
                    alert("User, you are Unauthorized. You will be redirected to login");
                    window.location.href = "login.html"
                    reject("User, you are Unauthorized. You will be redirected to login");
                },
                500: function (xhr) {
                    alert("Oops! " + xhr.responseText)
                    //window.location.href = "index.html"
                    reject("Oops! " + xhr.responseText);
                }
            },
            success: async function (data) {
                let myData = data["benefitIDs"];
                console.log("Data: " + myData);
                console.log("Data split: " + myData);
                //return myData.split(",");
                //resolve(myData.split(","));
                resolve(myData);

            },
            contentType: 'application/json',
            dataType: 'json'
        });
    });
}

async function editBenefit(benefitID){
    let title = $('#BenefitTitle').val();
    let description = $('#BenefitDescription').val();
    let points = $('#BenefitPoints').val();
    let a = document.getElementById("exampleFormControlSelect2")
    let category = a.options[a.selectedIndex].text
    let image = 'image' //correct name based on id = $('#imageUpload').val()

    let objArray = []
    for (let i = 1; i <= numberOfAttributes; i++) {
        if ($('#countBenefit' + i).val().length > 0) {
            let obj = {count: $('#countBenefit'+i).val()}
            if (document.getElementById("colorCheckbox1").checked && $('#colorBenefit'+i).val().length > 0) {
                Object.assign(obj, {color: parseInt($('#colorBenefit'+i).val())})
            } else {
                Object.assign(obj, {color: null})
            }
            if (document.getElementById("sizeCheckbox"+i).checked && $('#sizeBenefit'+i).val().length > 0) {
                Object.assign(obj, {size: $('#sizeBenefit'+i).val()})
            } else {
                Object.assign(obj, {size: null})
            }
            objArray.push(obj)
        }
    }
    numberOfAttributes = 1

    $.ajax({
        type: 'POST',
        url: 'api/shop/'+benefitID+'/edit',
        data: JSON.stringify({title: title, description: description, points: points,  category: category, attributes: objArray}),
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {
                alert("Benefit Edited!")
                window.location.href = "shopmanagement.js"
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

async function deleteBenefit(benefitID){
    $.ajax({
        type: 'DELETE',
        url: 'api/shop/'+benefitID+'/remove',
        cookie: cookiesConcat(),
        statusCode: {
            200: function () {
                window.location.href = "shopmanagement.html"
            },
            401: function () {
                alert("User, you are Unauthorized. You will be redirected to login");
                window.location.href = "login.html"
            },
            500: function (xhr) {
                alert("Oops! " + xhr.responseText)
                //window.location.href = "login.html"
            }
        },
        success: function () {
        },
        contentType: 'application/json',
        dataType: 'json'
    })
}

async function loopShopBenefits() {
    let data = await getAllBenefits()
    let benefits = ``
    for (let i = 0; i < data.length; i++) {
        let benefit = await getBenefitDetails(data[i])
        benefits += ` <li class="list-group-item">
          ${(i + 1) + ". " + benefit[1]}
          <div class="btn-group">
            <button class="btn btn-voice-primary edit-btn" id="EditButtonBenefitIndex${i}" onclick="getBenefitDetailsForEdit(${benefit[0]})">Edit</button>
            <button class="btn btn-voice-danger delete-btn" id="DeleteButtonBenefitIndex${i}" onclick="deleteBenefit(${benefit[0]})">Delete</button>
          <!--<button class="btn btn-primary edit-btn" id="EditButtonBenefitIndex"+i onclick="getBenefitDetailsForEdit(${benefit[0]})">Edit</button>
            <button class="btn btn-danger delete-btn" id="DeleteButtonBenefitIndex"+i onclick="deleteBenefit(${benefit[0]})">Delete</button> -->
          </div>
        </li>`
    }
    $("#listList").html(benefits)
}

let numberOfAttributes = 1
async function appendAttributes() {
    if ($('#countBenefit'+numberOfAttributes).val().length > 0 &&
        ((document.getElementById("colorCheckbox"+numberOfAttributes).checked) ||
            document.getElementById("sizeCheckbox"+numberOfAttributes).checked && $('#sizeBenefit'+numberOfAttributes).val().length > 0)) {
        let attributes = document.getElementById('attributes');
        numberOfAttributes++
        let newAttributes = `<div class="size-input container g-3">
              <div class = "row">
                <div class="col">
                  <label for="countBenefit${numberOfAttributes}">Count</label>
                  <input type="text" class="form-control" placeholder="Enter amount" id = "countBenefit${numberOfAttributes}" onchange="appendAttributes()">
                </div>
                <div class="col">
                  <label for="colorBenefit${numberOfAttributes}">Color</label>
                  <div class="input-group mb-3">
                    <div class="input-group-prepend">
                      <div class="input-group-text">
                        <input type="checkbox" aria-label="Checkbox for following text input" id="colorCheckbox${numberOfAttributes}" onclick="appendAttributes()">
                      </div>
                    </div>
                    <!--<input type="text" class="form-control" placeholder="Enter colour" id = "colorBenefit${numberOfAttributes}" aria-label="Text input with checkbox" onchange="appendAttributes()">-->
                    <select class="form-select" id = "colorBenefit${numberOfAttributes}" onchange="appendAttributes()">
                      <option value="0">Red</option>
                      <option value="1">Green</option>
                      <option value="2">Blue</option>
                      <option value="3">Yellow</option>
                      <option value="4">Black</option>
                      <option value="5">Purple</option>
                      <option value="6">Orange</option>
                      <option value="7">White</option>
                      <option value="8">Pink</option>
                    </select>
                  </div>
                </div>
                <div class="col">
                  <label for="sizeBenefit${numberOfAttributes}">Size</label>
                  <div class="input-group mb-3">
                    <div class="input-group-prepend">
                      <div class="input-group-text">
                        <input type="checkbox" aria-label="Checkbox for following text input" id="sizeCheckbox${numberOfAttributes}" onclick="appendAttributes()">
                      </div>
                    </div>
                    <input type="text" class="form-control" placeholder="Enter size" id = "sizeBenefit${numberOfAttributes}" aria-label="Text input with checkbox" onchange="appendAttributes()">
                  </div>
                </div>
              </div>
            </div>`
        attributes.insertAdjacentHTML('beforeend', newAttributes)
    }
}

async function appendAttributesForEdit(numberOfAttributesEdit) {
    let attributes = document.getElementById('attributes')
    for (let k = 2; k <= numberOfAttributesEdit; k++){
        numberOfAttributes++
        let newAttributes = `<div class="size-input container g-3">
                 <div class = "row">
                   <div class="col">
                     <label for="countBenefit${numberOfAttributesEdit}">Count</label>
                     <input type="text" class="form-control" placeholder="Enter amount" id = "countBenefit${numberOfAttributesEdit}" onchange="appendAttributes()">
                   </div>
                   <div class="col">
                     <label for="colorBenefit${numberOfAttributesEdit}">Color</label>
                     <div class="input-group mb-3">
                       <div class="input-group-prepend">
                         <div class="input-group-text">
                           <input type="checkbox" aria-label="Checkbox for following text input" id="colorCheckbox${numberOfAttributesEdit}" onclick="appendAttributes()">
                         </div>
                       </div>
                       <!--<input type="text" class="form-control" placeholder="Enter colour" id = "colorBenefit${numberOfAttributes}" aria-label="Text input with checkbox" onchange="appendAttributesForEdit()">-->
                    <select class="form-select" id = "colorBenefit${numberOfAttributes}" onchange="appendAttributesForEdit()">
                      <option value="0">Red</option>
                      <option value="1">Green</option>
                      <option value="2">Blue</option>
                      <option value="3">Yellow</option>
                      <option value="4">Black</option>
                      <option value="5">Purple</option>
                      <option value="6">Orange</option>
                      <option value="7">White</option>
                      <option value="8">Pink</option>
                    </select>                     
                     </div>
                   </div>
                   <div class="col">
                     <label for="sizeBenefit${numberOfAttributesEdit}">Size</label>
                     <div class="input-group mb-3">
                       <div class="input-group-prepend">
                         <div class="input-group-text">      
                           <input type="checkbox" aria-label="Checkbox for following text input" id="sizeCheckbox${numberOfAttributesEdit}" onclick="appendAttributesForEdit()">
                         </div>
                       </div>
                       <input type="text" class="form-control" placeholder="Enter size" id = "sizeBenefit${numberOfAttributesEdit}" aria-label="Text input with checkbox" onchange="appendAttributesForEdit()">
                     </div>
                   </div>
                 </div>
               </div>`
        attributes.insertAdjacentHTML('beforeend', newAttributes)
    }
}
