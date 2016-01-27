package applets.etsmtl.ca.news.db;

import applets.etsmtl.ca.news.model.Source;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by gnut3ll4 on 24/01/16.
 */
public class SourceDAO extends DAO<Source> {
    @Override
    public Source find(String key) {
        Source source = new Source();
        try {
            ResultSet result = this.connection
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY
                    ).executeQuery(
                            "SELECT * FROM sources WHERE key = '" +key+"'"
                    );
            if(result.first())
                source.setUrlImage(result.getString("url_image"));
                source.setType(result.getString("type"));
                source.setName(result.getString("name"));
                source.setKey(key);


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return source;
    }

    @Override
    public boolean isExisting(String key) {
        try {
            ResultSet result = this.connection
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY
                    ).executeQuery(
                            "SELECT key FROM sources WHERE key = '" +key+"'"
                    );
            if(result.first())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Source> findAll() {
        //TODO
        return null;
    }

    public void add(Source source) {
        try {

            String req_insert_source = "INSERT INTO sources (key, name, type, url_image, value) VALUES (?,?,?::type_source,?,?)";
            PreparedStatement preparedStatement = ConnectionSingleton.getInstance().prepareStatement(req_insert_source);

            preparedStatement.setString(1, source.getKey());
            preparedStatement.setString(2, source.getName());
            preparedStatement.setString(3, source.getType());
            preparedStatement.setString(4, source.getUrlImage());
            preparedStatement.setString(5, source.getValue());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
