


async function loopAllNewsManagement(){
    let data = await getAllNews()
    console.log("data"+data[0].length)
    let allNews = ``
    for (let i = 0; i < data[0].length; i++) {
        let news = await getNewsDetails(data[0][i])
        console.log("News Details: "+news)
        allNews += `   <tr>
                <th scope="row">
                    <div class="list-group" style = "--bs-list-group-border-color: transparent; --bs-list-group-action-active-bg: transparent; --bs-list-group-active-color: transparent; --bs-list-group-action-hover-bg: transparent " >
                        <a  class="list-group-item list-group-item-action" style="padding:0;">${(i + 1) + ". "}</a>
                    </div>
                </th>
                <td>
                    <div class="list-group" style = "--bs-list-group-border-color: transparent; --bs-list-group-action-active-bg: transparent; --bs-list-group-active-color: transparent; --bs-list-group-action-hover-bg: transparent " >
                        <a  class="list-group-item list-group-item-action" style="padding:0;">${news[1]}</a>
                    </div>
                </td>
                <td>
                    <button type="button" class="btn btn-danger delete-btn" id="DeleteButtonNewsIndex${i}" onclick="deleteNews(${news[0]})">Delete</button>
                </td>
            </tr>`
    }
    $('#listListNews').html(allNews)
    console.log("loop ended")
}