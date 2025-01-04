$(document).ready(function() {
            $("#locales").change(function () {
                var selectedOption = $('#locales').val();
                    window.location.replace(window.location.pathname +'?lang=' + selectedOption);
           });
        });