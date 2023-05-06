$('document').ready(function (){
    $('table #editButton').on('click', function (event){
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (product, status){
            $('#nameEdit').val(product.name);
            $('#categoryEdit').val(product.categories);
            $('#descriptionEdit').val(product.description);
            $('#quantityEdit').val(product.currentQuantity);
            $('#priceEdit').val(product.costPrice);
            $('#imageProductEdit').val(product.imageProduct);
        });
        $('#addProductModal').modal();
    });
});