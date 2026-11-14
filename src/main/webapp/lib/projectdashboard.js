async function loadGraph(questions) {
    let allQuestions = []
    for (let i = 0; i < questions.length; i++) {
        let question = questions[i]
        allQuestions.push(question["text"])
    }
    // setup
    const data = {
        labels: ['Q1', 'Q2', 'Q3', 'Q4', 'Q5', 'Q6', 'Q7'],
        datasets: [{
            label: '#1',
            data: [18, 12, 6, 9, 12, 3, 9],
            backgroundColor: 'rgba(255,206,187,0.44)',
            borderColor: '#5C4B3A',
            borderWidth: 1
        }, {
            label: '#2',
            data: [18, 12, 6, 9, 12, 3, 9],
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
        }]
    };

    // config
    const config = {
        type: 'bar',
        data,
        options: {
            scales: {
                y: {
                    title: {
                        display: true,
                        text: '# of votes',
                        align: 'end'
                    }
                },
                x:{
                    title: {
                        display: true,
                        text: 'Questions',
                        align: 'end'
                    }
                }
            }
        }
    };

    // render init block
    const myChart = new Chart(
        document.getElementById('myChart'),
        config
    );

}