async function receiveBenefit() {
    let queryString = window.location.search;
    let urlParams = new URLSearchParams(queryString);
    let passedVariable = parseInt(urlParams.get('var'));
    let data = await getBenefitDetails(passedVariable)
    let benefit = `<div class="rightInfo"><br>
      <b class="item-title">${data[1]}</b>
      <br>
      <br>
      <b class="item-description-title">Item Description</b>
      <p class= "information-description">${data[2]}</p>
      <hr class="hr" />
      <b class="item-description-title">Point cost</b>
      <p class="information-points">${data[3]} points</p>
      <hr class="hr" />
      <b class="item-description-title">Color</b>
      <br>
      <div class="buttons">
          <button type="button" class="btn btn-danger btn-lg btn-circle" id="0" style="display: none" ></button>
          <button type="button" class="btn btn-success btn-lg btn-circle" id="1" style="display: none" ></button>
          <button type="button" class="btn btn-primary btn-lg btn-circle" id="2" style="display: none" ></button>
          <button type="button" class="btn btn-warning btn-lg btn-circle" id="3" style="display: none" ></button>
          <button type="button" class="btn btn-dark btn-lg btn-circle" id="4" style="display: none" ></button>
          <button type="button" class="btn btn-purple btn-lg btn-circle" id="5" style="display: none" ></button>
          <button type="button" class="btn btn-orange btn-lg btn-circle" id="6" style="display: none"></button>
          <button type="button" class="btn btn-light btn-lg btn-circle" id="7" style="display: none" ></button>
          <button type="button" class="btn btn-pink btn-lg btn-circle" id="8" style="display: none"></button>
      </div>
      
      

      
      <hr class="hr" />
      <b class="item-description-title">Size</b>

      <div class="dropdown" style="margin-bottom: 80px;">
        <select class="form-select" id="selectSizeMenu">
          <option value="0">---Select a color---</option>
        </select>
      </div>

      <button type="button" class="btn btn-voice btn-lg btn-block" style="margin-bottom: 0;">Add to shopping cart</button>
    </div>`


    $("#benefitInfo").html(benefit)

    await showButtons(data);
}

async function colorSelection(id, data){
    const targetButton = document.getElementById(id);
    const divElement = document.querySelector('.buttons');
    const buttons = divElement.querySelectorAll('.btn');
    let attributes = data[5];
    var selectElement = document.getElementById("selectSizeMenu");

    //remove the outlines of all the buttons
    buttons.forEach((button) => {
        button.style.outline = 'none';
    });
    //set the outline of the target button
    targetButton.style.outline = '2px solid green'

    //remove all the elements
    while (selectElement.options.length > 0) {
        selectElement.remove(0);
    }
    //assigns size to right color
    var option = document.createElement("option");
    for (let i = 0; i < attributes.length; i++) {
        if (attributes[i]["color"] == id && attributes[i]["size"]!=null){
            var option = document.createElement("option");
            option.value = i.toString();
            option.text = attributes[i]["size"];
            selectElement.add(option);
        }
    }
    //displays when no sizes are available
    if (selectElement.options.length == 0){
        option.value = "no size";
        option.text = "---There are no different sizes for this item available---";
        selectElement.add(option);
    }
}

async function showButtons(data){
    let attributes = data[5]
    let colors = []
    const divElement = document.querySelector('.buttons');
    const buttons = divElement.querySelectorAll('.btn');
    for (let i = 0; i < attributes.length; i++) {
        colors.push(attributes[i]["color"]);
    }

    //displays selected colors by admin for item
    for (let i = 0; i < colors.length; i++) {
        buttons.forEach((button) => {
            if (button.id == colors[i]){
                button.style.display= "inline"
                button.addEventListener('click', () => {
                    colorSelection(button.id, data);
                });
            }
        });
    }
}