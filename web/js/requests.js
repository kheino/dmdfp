/**
 * Created by khk on 3/2/14.
 */
function listItems()
{
    ajaxGet("/dmdfp/rest/shop/listItems", function(resp) {
        var items = JSON.parse(resp);
        var grid = $("#product-grid");

        for (var i = 0; i < items.length; i++)
        {
            var item = items[i];
            var container = $('<div class="product"></div>');

            // Item id
            container.append(sprintf('Item ID: %d', item.id));

            // Item image
            container.append($(sprintf(
                '<a class="product-image" href="javascript:viewItem(%d)">'
                + '<img alt="%s" src="%s" />'
                + '</a>',
                item.id, item.name, item.url
            )));

            // Item name
            var info = $('<div class="product-info"></div>');
            container.append(info);
            info.append($(sprintf(
                '<div class="product-name">'
                + '<a href="javascript:viewItem(%d)">%s</a>'
                + '</div>',
                item.id, item.name
            )));

            // Item description
            info.append($(sprintf(
                '<div class="product-description">%s</div>',
                item.description
            )));

            // Item price
            info.append($(sprintf(
                '<div class="product-price">DKK %d</div>',
                item.price
            )));

            grid.append(container);
        }
    });
}