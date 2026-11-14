function transitionToItemInfo(benefitID) {
    window.location.href = "shopitem.html?var=" + encodeURIComponent(benefitID)
}
async function generateBenefitCard() {
    let data = await getAllBenefits()
    //console.log("data " + data)
    let benefits = ``
    for (let i = 0; i < data.length; i++) {
        let benefit = await getBenefitDetails(data[i])
        //console.log("benefit " + benefit)
        let id = i+1
        benefits += `<div class="card shadow">
      <img src="images/empty.jpg" class="card-img-top" id="image${id}" alt="image">
      <div class="title-points-card">
        <h5 class="card-title fw-bold">${benefit[1]}</h5>
        <h5 class="text-card fw-semibold" style="text-align: right; padding-right: 5px">${benefit[3]}</h5>
        <h5 class="form-label" style="font-size: 15px; padding-top: 4px">Points</h5>
      </div>
      <p class="card-text">${benefit[2]}</p>
      <a class="btn btn-voice" data-bs-toggle="modal" data-bs-target=".bd-example-modal-lg" onclick="transitionToItemInfo(${benefit[0]})">Redeem</a>
    </div>`
        //await downloadImage($("#image"+(i+1)), "shop", benefit[0], "image0")
    }
    $("#benefitCards").html(benefits)
    for (let i = 0; i < data.length; i++) {
        let id = i+1
        let benefit = await getBenefitDetails(data[i])
        $("#image"+id).attr('src', "api/filerepo/fetch-image/shop/"+benefit[0]+"/image0")
    }
}