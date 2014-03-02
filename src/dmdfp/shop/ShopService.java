package dmdfp.shop;

import dmdfp.share.Cloudy;
import dmdfp.share.Environment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.transform.XSLTransformer;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by khk on 3/2/14.
 */
@Path("shop")
public class ShopService
{
    private static final String
            ENV = "env",
            DOCUMENT = "document",
            ITEM = "item",
            ITEM_ID = "itemID",
            ITEM_NAME = "itemName",
            ITEM_URL = "itemURL",
            ITEM_PRICE = "itemPrice",
            ITEM_STOCK = "itemStock",
            ITEM_DESCRIPTION = "itemDescription",
            ID = "id",
            NAME = "name",
            URL = "url",
            PRICE = "price",
            STOCK = "stock",
            DESCRIPTION = "description";

    @Context
    HttpSession session;

    @Context
    ServletContext context;

    Environment env;

    @PostConstruct
    public void init()
    {
        if (context.getAttribute(ENV) == null)
        {
            String basePath = context.getRealPath("/");
            Environment env = new Environment(basePath);
            context.setAttribute(ENV, env);
        }

        env = (Environment) context.getAttribute(ENV);
    }

    @GET
    @Path("listItems")
    public String getItems()
    {
        try
        {
            Document resp = Cloudy.listItems(env.getCloudSchemaPath());
            List<Element> children = resp.getRootElement().getChildren(ITEM, Cloudy.NS);

            JSONArray arr = new JSONArray();
            JSONObject obj;
            for (Element c : children)
            {
                obj = new JSONObject();
                obj.put(ID, getInt(c, ITEM_ID));
                obj.put(NAME, getText(c, ITEM_NAME));

                URI uri = new URI(getText(c, ITEM_URL));
                if (!uri.isAbsolute())
                {
                    uri = new URI(context.getContextPath() + uri);
                }

                obj.put(URL, uri.toString());
                obj.put(PRICE, getInt(c, ITEM_PRICE));
                obj.put(STOCK, getInt(c, ITEM_STOCK));

                Element desc = c.getChild(ITEM_DESCRIPTION, Cloudy.NS)
                                .getChild(DOCUMENT, Cloudy.NS)
                                .clone();
                obj.put(DESCRIPTION, toHtml(desc));

                arr.put(obj);
            }

            //TODO: You can create a MessageBodyWriter so you don't have to call toString() every time
            return arr.toString();
        }
        catch (IOException|JDOMException|URISyntaxException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private int getInt(Element elm, String child)
    {
        return Integer.parseInt(getText(elm, child));
    }

    private String getText(Element elm, String child)
    {
        return elm.getChild(child, Cloudy.NS).getText();
    }

    private String toHtml(Element elm)
    {
        try
        {
            XSLTransformer tr = new XSLTransformer(
                    env.getItemDescriptionStylesheet());

            Document doc = tr.transform(new Document(elm));

            return new XMLOutputter(Format.getPrettyFormat())
                    .outputString(doc.getRootElement());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}