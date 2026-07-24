export function useAddressSearch(onAddressComplete, onDeliveryAddressComplete) {
  const execAddress = () => {
    new window.daum.Postcode({
      oncomplete: (data) => {
        onAddressComplete(data.roadAddress);
      },
    }).open();
  };

  const execDeliveryAddress = () => {
    new window.daum.Postcode({
      oncomplete: (data) => {
        onDeliveryAddressComplete(data.roadAddress);
      },
    }).open();
  };

  return { execAddress, execDeliveryAddress };
}
