package jayray.net.partners;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import jayray.net.orders.Customer;
import jayray.net.orders.CustomerDao;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gnut3ll4 on 04/12/15.
 */
@Path("partners")
public class PartnersResource {

    String PARTNERS_URL = "http://www.clubapplets.ca/partenaires";
    String COL_MD_REGEX = "^col-sm-([0-9])$";

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ArrayList<Partner> getPartners() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.clubapplets.ca/partenaires/")
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            Document document = Jsoup.parseBodyFragment(response.body().string());
            Elements partnersRow = document.select("div[class=row partners]");

            Elements partnersDivs = partnersRow.select("div[class~=" + COL_MD_REGEX + "]");

            ArrayList<Partner> partners = new ArrayList<Partner>();

            Pattern p = Pattern.compile(COL_MD_REGEX);


            for (Element div : partnersDivs) {
                Matcher matcher = p.matcher(div.attr("class"));
                int index = 0;
                if(matcher.find()) {
                    index = Integer.valueOf(matcher.group(1));
                }

                String url = div.getElementsByTag("a").first().attr("href");
                String imageUrl = div.getElementsByTag("img").first().attr("src");
                String name = div.getElementsByTag("h3").first().text();

                partners.add(new Partner(
                        url,
                        index,
                        imageUrl,
                        name
                        ));
            }

            return partners;


        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

}
