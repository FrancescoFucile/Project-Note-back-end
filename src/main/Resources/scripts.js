        function load(uri) {
                     var xhttp = new XMLHttpRequest();
                     xhttp.onreadystatechange = function() {
                          if (this.readyState == 4 && this.status == 200) {
                              return this.responseText;
                           }
                     };
                     xhttp.open("GET", "http://localhost:5000/"+uri, true);
                     xhttp.send();
                 }

                 function search() = {
                     document.getElementById("span").innetHTML = load("uritest");
                 }