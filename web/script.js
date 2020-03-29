function nextStep(i, j) {
    $.ajax({
        url: "game",
        type: "GET",
        data: ({
            i: i,
            j: j
        }),
        success: function (data) {
            try {
                data = JSON.parse(data);

                var field = data.gameField.field;
                var size = data.gameField.size;
                for (var k = 0; k < size; k++) {
                    for (var l = 0; l < size; l++) {
                        var index = String(k * size + l);
                        var cell = document.getElementById(index);
                        cell.innerText = field[k][l];
                    }
                }
                if (data.isOver) {
                    var winnerMessageParagraph = document.getElementById("winnerMessage");
                    winnerMessageParagraph.style.display = "block";
                    winnerMessageParagraph.innerText = data.winnerMessage;
                }
            } catch (e) {
                alert(data);
            }
        }
    });
}