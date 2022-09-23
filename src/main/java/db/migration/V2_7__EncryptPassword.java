package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.ResultSet;
import java.sql.Statement;


public class V2_7__EncryptPassword extends BaseJavaMigration {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void migrate(Context context) throws Exception {
        try (Statement select = context.getConnection().createStatement()) {
            try (ResultSet rows = select.executeQuery("SELECT id,password FROM usertable WHERE password NOT LIKE '{$2a}%'")) {
                while (rows.next()) {
                    int id = rows.getInt(1);
                    String password = rows.getString(2);
                    String hashedPassword = passwordEncoder.encode(password);
                    try (Statement update = context.getConnection().createStatement()) {
                        update.execute("UPDATE usertable SET password='" + hashedPassword + "' WHERE id =" + id);
                    }
                }
            }
        }
    }
}









