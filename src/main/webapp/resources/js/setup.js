/**
 * Created by PaulRyan on 3/2/2016.
 */

$(document).ready(function () {
    Dropzone.autoDiscover = false;
    var token = $("input[name='_csrf']").val();
    $("#categoryAdd").click(function () {
        var data = {};
        var url = "/trainTag?_csrf=" + token;
        var categoryName = $('#inputCategory').val();
        var tags = $('#inputTags').tagsinput("items");
        var tr = "<tr><td>" + categoryName + "</td><td><a href='#!'><i class='fa fa-times'></i></a></td></tr>";
        $('#categoryTable').append(tr);
        data.name = categoryName;
        data.data = tags;
        post(data, url);
        $('#inputCategory').val("");
        $('#inputTags').tagsinput("removeAll");
    });
    $("#inputStartYear").on("change", function () {
        var value = $(":selected", this).val();
        value = parseFloat(value) + 1;
        var select = document.getElementById("inputEndYear");
        $("#inputEndYear").empty();
        for (var i = value + 50; i >= value; --i) {
            var option = document.createElement('option');
            option.text = option.value = i;
            select.add(option, 0);
        }
    });
    $('#category_upload').dropzone({
        paramName: 'file',
        clickable: true,
        previewsContainer: '#category_upload_preview',
        autoProcessQueue: false,
        init: function() {
            var dropzone = this;
            $('#category_upload_btn').click(function() {
                dropzone.processQueue();
            });
            dropzone.on('sending', function(file, xhr, formData) {
                formData.append('category_name', $('#category_name').val());
            });
        }
    });
});

function post(data, url) {
    var jsonData = JSON.stringify(data);
    console.log(jsonData);
    $.ajax({
        type: 'POST',
        url: url,
        dataType: 'json',
        data: jsonData,
        contentType: "application/json",
        success: function(e) {
            console.log("success_save");
        }
    });
}