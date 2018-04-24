/**
 * When the content has loaded, go ahead and attach a listener to the form submission.
 *  1. Dead simple validation as this is not even close to prod ready.
 *  2. Submit and get results if ok.
 *  3. Error handling as needed.
 *
 *  TODO - Make aria and ADA compatible 
 */

//Create a result component. Not really a component, but...
var createComponent = function(body) {
  //Ugh, ES6 is so much better. Bindings, I miss you so...
  let component = '<dl class="row">';
  for (var i = 0; i < body.length; i++) {
    component += addDescription(body[i]);
  }
  component += '</dl>';
  return component;
}

//Add description item, to the definition list.
var addDescription = function(item) {
  console.log(item);
  let electionDate = new Date(item.date).toLocaleDateString("en-US");
  let description = '<dt class="col-sm-3">Election Name</dt><dd class="col-sm-9">'+ item.description +'</dd>';
  description += '<dt class="col-sm-3">Election Date</dt><dd class="col-sm-9">'+ electionDate +'</dd>';
  description += '<dt class="col-sm-3">Website</dt><dd class="col-sm-9"><a href="'+ item.website +'" target="blank">'+ item.website +'</a></dd>';
  description += '<dt class="col-sm-3">Polling Places</dt><dd class="col-sm-9"><a href="'+ item['polling-place-url'] +'" target="blank">'+ item['polling-place-url'] +'</a></dd>';
  return description;
}

//Search has not been found. Reset.
var notFound = function(frm) {
  frm.reset();
  alert('Sorry, could not find any elections for your address. Please try again.');
}

//TODO - so sloppy, should use server side HTML or RXJS!
var found = function(body, searchResults) {
  let successAlert = document.createElement('div');
  successAlert.innerHTML = '<div class="alert alert-success"><p>Success! Election Information Found.</p></div>';
  searchResults.appendChild(successAlert);
  let results = document.createElement('div');
  results.innerHTML = createComponent(body);
  searchResults.appendChild(results);
}

/**
 * Run API Finder.
 */
var search = function(data, searchResults, frm) {
  //Create XHR Request
  let xhr = new XMLHttpRequest();
  xhr.open('POST', '/search', true);
  xhr.timeout = 5000;
  xhr.send(data);
  xhr.onreadystatechange = function() {
    if(xhr.readyState === XMLHttpRequest.DONE) {
      if (xhr.status === 200) {
          //Take the JSON and show to the user.
          let res = JSON.parse(xhr.responseText);
          let body = res.result.body;
          if (body.length) {
            found(body, searchResults);
          } else {
            notFound(frm);
          }
      } else {
        notFound(frm);
      }
    }
  }
}

/**
 * Validate Form Items - show a generic message.
 *  This is the simplest I could think of to have some sort of UI.
 *  Bad validation, but stops you first.
 */
var validate = function(elem) {
    let data = new FormData(elem);
    if (data.get('street')
        && data.get('city')
        && data.get('state')
        && data.get('zip')) {
          return data;
    } else {
      return false;
    }
}

/**
 * Initialize handlers for serch.
 */
var init = function() {
  let searchResults = document.querySelector('#search-results');
  let frm = document.querySelector('form#address-search');
  frm.addEventListener('submit', function(e) {
    e.preventDefault();
    //Clear out the search container.
    searchResults.innerHTML = '';
    //Validate the form data.
    let formData = validate(this);
    if (formData) {
      search(formData, searchResults, this);
    } else {
      alert('Please fill out the required form fields before continuing');
    }
  }, false)
}

/**
 * Wait for content loaded - not for older browsers.
 */
document.addEventListener("DOMContentLoaded", function() {
  init();
});
