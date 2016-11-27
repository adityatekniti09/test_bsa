var app = angular.module('bsaApp', []);
var searchUrl = "/bin/bsa/searchresults"
	app
	.controller(
			'assetShareController',
			[
			 '$scope',
			 '$http',
			 function($scope, $http) {
				 $scope.data = {};
				 $scope.data.assetObjects = {};
				 $scope.data.searchTermText = "";
				 $scope.data.offset = 0;
				 $scope.data.resultsPerPage = 12;
				 $scope.data.isPreviousDisable = true;
				 $scope.searchButton = function() {
					 doSearch();
				 }
				 $scope.data.allDownloadPaths = [];
				 $scope.previousButton = function() {
					 $scope.data.offset -= $scope.data.resultsPerPage;
					 doSearch();
					 if ($scope.data.offset < $scope.data.resultsPerPage) {
						 $scope.data.isPreviousDisable = true;
					 } else {
						 $scope.data.isPreviousDisable = false;
					 }
					 $scope.data.isNextDisable = false;
				 }

				 $scope.nextButton = function() {
					 $scope.data.offset += $scope.data.resultsPerPage;
					 doSearch();
					 if ($scope.data.totalMatches
							 / $scope.data.resultsPerPage < $scope.data.resultsPerPage) {
						 $scope.data.isNextDisable = true;
					 } else {
						 $scope.data.isNextDisable = false;
					 }
					 $scope.data.isPreviousDisable = false;

				 }
				 $scope.searchOnLoad = function() {
					 doSearch();
				 }
				 $scope.addToDownload = function(downloadPath,
						 rendition) {
					 if (rendition == "low") {
						 downloadPath += "/jcr:content/renditions/cq5dam.thumbnail.140.100.png";
					 } else if (rendition == "medium") {
						 downloadPath += "/jcr:content/renditions/cq5dam.thumbnail.319.319.png";
					 } else if (rendition == "high") {
						 downloadPath += "/jcr:content/renditions/cq5dam.web.1280.1280.jpeg";
					 }
					 $scope.data.allDownloadPaths.push({
						 "downloadPath" : downloadPath
					 });
				 }
				 $scope.downloadAll = function() {
					 var zip = new JSZip();
					 var folder = zip.folder(new Date());
					 for (var i = 0; i < $scope.data.allDownloadPaths.length; i++) {
						 var dlPath = $scope.data.allDownloadPaths[i].downloadPath;
						 var splitDlPath = dlPath.split("/");
						 var titleImage = splitDlPath[splitDlPath.length - 1];
					 }
					 $scope.data.allDownloadPaths = [];
				 }

				 $scope.hoverIn = function() {
					 this.hoverEdit = true;
				 };

				 $scope.hoverOut = function() {
					 this.hoverEdit = false;
				 };

				 $scope
				 .$watch(
						 'data.searchTermText',
						 function() {
							 // alert($scope.data.searchtermtext);
							 var searchTerm = $scope.data.searchTermText;
							 if (searchTerm != undefined
									 && searchTerm.length > 3) {
								 $http
								 .get(
										 searchUrl,
										 {
											 params : {
												 "searchText" : searchTerm+ "*",
												 "offSet" : 0,
												 "resultsPerPage" : 5
											 }
										 })
										 .then(
												 function(response) {
													
													 document.getElementById("searchList").innerHTML = "";
													 var list = document.getElementById('searchList');
													 if (response.data.assetsObject.length >1){
														 for (var i = 0; i < response.data.assetsObject.length; i++) {
															 var option = document.createElement('option');
															 option.value = response.data.assetsObject[i].title;
															 list .appendChild(option);
														 } 
													 } 
												 })
							 };
						 });

				 function downloadURI(uri, name) {
					 var a = document.createElement('a');
					 a.href = uri;
					 a.download = name;
					 document.body.appendChild(a);
					 a.click();
					 document.body.removeChild(a);
				 }

				 function doSearch() {
					 var resultSearchText = $scope.data.searchTermText;
					 $http
					 .get(
							 searchUrl,
							 {
								 params : {
									 "searchText" : resultSearchText+ "*",
									 "offSet" : $scope.data.offset,
									 "resultsPerPage" : 12
								 }
							 })
							 .then(
									 function(response) {
										 var resultsArray = [];
										 for (var i = 0; i < response.data.assetsObject.length; i++) {
											 var isAlreadyforDownload = false;
											 var dlPath = response.data.assetsObject[i].downloadPath;
											 for ( var j in $scope.data.allDownloadPaths) {
												 if (dlPath == j) {
													 isAlreadyforDownload = true;
												 }
											 }

											 var resultsObj = {
													 title : response.data.assetsObject[i].title,
													 downloadPath : dlPath,
													 isDownload : isAlreadyforDownload,
													 gridImage : dlPath
													 + "/jcr:content/renditions/cq5dam.thumbnail.319.319.png"
											 };
											 resultsArray.push(resultsObj);
										 }
										 $scope.data.assetObjects = resultsArray;
										 $scope.data.offset = response.data.offset;
										 $scope.data.hitsPerPage = response.data.hitsPerPage;
										 $scope.data.totalMatches = response.data.totalMatches;
									 })
				 };
			 } ]);

app.constant('keyCodes', {
	esc : 27,
	space : 32,
	enter : 13,
	tab : 9,
	backspace : 8,
	shift : 16,
	ctrl : 17,
	alt : 18,
	capslock : 20,
	numlock : 144
})
app.directive('keyBind', [ 'keyCodes', function(keyCodes) {
	function map(obj) {
		var mapped = {};
		for ( var key in obj) {
			var action = obj[key];
			if (keyCodes.hasOwnProperty(key)) {
				mapped[keyCodes[key]] = action;
			}
		}
		return mapped;
	}

	return function(scope, element, attrs) {
		var bindings = map(scope.$eval(attrs.keyBind));
		element.bind("keydown keypress", function(event) {
			if (bindings.hasOwnProperty(event.which)) {
				scope.$apply(function() {
					scope.$eval(bindings[event.which]);
				});
			}
		});
	};
} ]);