Drupal.behaviors.serviceSearch = {
  attach(context) {
  	const querystring = window.location.search;
  	const params = new URLSearchParams(querystring);
  	const keyword = params.get('keyword');
  	const sort = params.get('sort_by');
    const sortSelect = context.getElementsByClassName('form-select__dropdown')[0];

    var serviceSearchForm = context.getElementsByClassName('services-search__form')[0];
    serviceSearchForm = serviceSearchForm == null ? context.getElementById('views-exposed-form-search-services') : serviceSearchForm;
  	
    if (serviceSearchForm != null) {
      const formInput = serviceSearchForm.getElementsByTagName('input')[0];

      if (keyword != null && keyword.length > 0) {
        formInput.value = keyword;
      }
    }
  	
    if (sortSelect != null) {

      if (sort != null && sort.length > 0) {
        sortSelect.value = sort;
      }

      
      sortSelect.addEventListener('change', (e) => {
        var url = window.location.href.split("?")[0];
        params.append('sort_by', e.target.value);
        window.location = url + "?" + params.toString();
      });
    }
  }
}