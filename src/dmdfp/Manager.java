package dmdfp;

import org.jdom2.Document;
import org.jdom2.Element;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * Created by khk on 2/21/14.
 */
@ManagedBean
@SessionScoped
public class Manager implements Serializable
{

    private static final String
        SUCCESS = "SUCCESS",
        FAILURE = "FAILURE";

    private int stockAdjustment;

    transient private String adminName = "dmdfpadmin";

    @ManagedProperty("#{itemList}")
    private ItemList items;

    @ManagedProperty("#{user}")
    private User user;

    @ManagedProperty("#{auth}")
    private Authorization auth;

    @ManagedProperty("#{env}")
    private Environment env;

    public int getStockAdjustment() {
        return stockAdjustment;
    }

    public void setStockAdjustment(int stockAdjustment) {
        this.stockAdjustment = stockAdjustment;
    }

    public ItemList getItems() {
        return items;
    }

    public void setItems(ItemList items) {
        this.items = items;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAuth(Authorization auth) {
        this.auth = auth;
    }

    public boolean isUserAdmin()
    {
        return (auth.isLoggedIn()
                && user.getUsername() != null
                && user.getUsername().equals(adminName));
    }

    public String createItem()
    {
        Item item = items.getCurrentItem();

        try {
            Cloudy.createItem(
                    item.getName(),
                    item.getUrl(),
                    item.getPrice(),
                    item.getDescription(),
                    env.getCloudSchemaPath());

            items.listItems();
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return FAILURE;
    }

    public String modifyItem()
    {
        Item item = items.getCurrentItem();

        try {
            Cloudy.modifyItem(
                    item.getId(),
                    item.getName(),
                    item.getUrl(),
                    item.getPrice(),
                    item.getDescription());

            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return FAILURE;
    }

    public String adjustStockItem()
    {
        Item item = items.getCurrentItem();

        try {
            Cloudy.adjustItemStock(item.getId(), stockAdjustment);
            item.setStock(item.getStock() + stockAdjustment);
            return SUCCESS;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return FAILURE;
    }

    public void setEnv(Environment e)
    {
        env = e;
    }
}
