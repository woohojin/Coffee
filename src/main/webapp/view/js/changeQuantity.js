function increaseQuantity(object) {
    let quantity = object.value;
    quantity += 1;
    object.value = quantity;
}

function decreaseQuantity(object) {
    let quantity = object.value;
    quantity -= 1;
    object.value = quantity;
}