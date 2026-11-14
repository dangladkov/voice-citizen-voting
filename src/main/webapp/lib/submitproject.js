function addQuestionV2() {
    let questionList = document.getElementById("questionsList")
    document.getElementById("questionCount").stepUp()
    let newQuestion = `<li class="question list-group-item" id="questionGroup${$("#questionCount").val()}">
            <div>
              <input class="labelFit form-control" id="question${$("#questionCount").val()}" type="text" maxlength="300" placeholder="Question title" required/>
              <br>

              <ul class="answerList" id="answerGroup${$("#questionCount").val()}">
                <li class="answer" style="position: relative;">
                  <input class="form-control" id="answer${$("#questionCount").val()*10+1}" type="text" maxlength="100" placeholder="Answer" required/>
                  <i class="deleteAnswer fa-solid fa-trash" onclick="deleteAnswer(this)" hidden></i>
                </li>
                <li class="answer" style="position: relative;">
                  <input class="form-control" id="answer${$("#questionCount").val()*10+2}" type="text" maxlength="100" placeholder="Answer" required/>
                  <i class="deleteAnswer fa-solid fa-trash" onclick="deleteAnswer(this)" hidden></i>
                </li>
              </ul>

              <br>
              <div class="d-grid gap-2 d-md-block">
                <button type="button" class="btn btn-outline-info btn-sm" id="${$("#questionCount").val()}" onclick="addAnswerV2(this)">Add answer</button>
                <button type="button" class="deleteBtn btn btn-outline-danger btn-sm" onclick="deleteQuestion(this)">Delete question</button>
              </div>
            </div>
          </li>`
    questionList.insertAdjacentHTML('beforeend', newQuestion)
}

function addAnswerV2(addBtn) {
    let answerList = document.getElementById(`answerGroup${addBtn.id}`)
    let lastID = addBtn.parentElement.parentElement.querySelector(".answerList li:last-child").querySelector(".form-control").id
    let id = parseInt(lastID.replace("answer", ""))
    let newAnswer = `<li class="answer" style="position: relative;">
                  <input class="form-control" id="answer${id+1}" type="text" maxlength="100" placeholder="Answer" required/>
                  <i class="deleteAnswer fa-solid fa-trash" onclick="deleteAnswer(this)"></i>
                </li>`
    answerList.insertAdjacentHTML('beforeend', newAnswer)
    addBtn.parentElement.parentElement.querySelector(".answerList li:last-child").querySelector(".deleteAnswer").hidden = false;
}

function refreshAnswerIDs(btn) {
    let id = parseInt(btn.parentElement.parentElement.id.replace("answerGroup", ""))

    $("#answerGroup"+id).getElementsByTagName("li").each(function () {
        this.input.val()
    })
}

// function addQuestion() {
//     let questionClone = document.querySelector(".question");
//     let questionToAdd = questionClone.cloneNode(true);
//     var questionFields = questionToAdd.querySelectorAll('input, textarea');
//     questionFields.forEach(function(input) {
//         input.value = '';
//     });
//
//     var itemsList = questionToAdd.querySelector(".answerList").getElementsByTagName("li");
//
//     var i = itemsList.length - 1;
//     while (i >= 0) {
//
//         // do something with items[i], which is a <li> element
//         if (itemsList[i].querySelector(".deleteAnswer").hidden === false) {
//             console.log(i + " removed");
//             itemsList[i].remove();
//         }
//         i--;
//     }
//     //
//     document.getElementById("questionsList").appendChild(questionToAdd);
//     document.getElementById("questionCount").stepUp();
//     document.querySelectorAll(".deleteBtn")[document.getElementById("questionCount").value - 1].hidden = false;
// }

function deleteQuestion(pressedDeleteBtn) {
    document.getElementById("questionCount").stepDown();
    pressedDeleteBtn.parentElement.parentElement.parentElement.remove();
}

// function addAnswer(addBtn) {
//     let answerClone = document.querySelector(".question").querySelector(".answer");
//     let answerToAdd = answerClone.cloneNode(true);
//     var answerFields = answerToAdd.querySelectorAll('input, textarea');
//     answerFields.forEach(function(input) {
//         input.value = '';
//     });
//     addBtn.parentElement.parentElement.querySelector(".answerList").appendChild(answerToAdd);
//     addBtn.parentElement.parentElement.querySelector(".answerList li:last-child").querySelector(".deleteAnswer").hidden = false;
// }

function deleteAnswer(clickedDeleteBtn) {
    clickedDeleteBtn.parentElement.remove();
}