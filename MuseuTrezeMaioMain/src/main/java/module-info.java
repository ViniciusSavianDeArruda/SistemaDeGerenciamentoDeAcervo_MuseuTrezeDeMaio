module br.com.museu.museutrezemaio {

    requires javafx.controls;

    requires javafx.fxml;



    requires java.sql;

    requires com.microsoft.sqlserver.jdbc;



    opens br.com.museu.museutrezemaio to javafx.fxml;



    opens br.com.museu.controller to javafx.fxml;





    opens br.com.museu.model to javafx.base, javafx.fxml;



    exports br.com.museu.museutrezemaio;

}