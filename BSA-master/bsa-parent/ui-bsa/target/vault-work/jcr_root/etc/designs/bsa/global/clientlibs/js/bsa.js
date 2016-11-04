var app = angular.module('bsaApp',[]);
var searchUrl ="/bin/bsa/searchresults"
	app.controller('assetShareController', ['$scope', '$http', function($scope, $http) {
		$scope.data = {};
		$scope.data.assetObjects ={};
		
		$scope.searchButton = function(){
			console.log($scope.data.searchText)
			doSearch();
		}

		$scope.previousButton = function(){
			$scope.data.offset -= $scope.data.resultsPerPage;
			doSearch();
		}

		$scope.nextButton = function(){
			$scope.data.offset += $scope.data.resultsPerPage;
			doSearch();
		}
		$scope.searchOnLoad = function(){
			//console.log(" asf" + $scope.data.resultsPerPage);
			 doSearch();
		}
		function doSearch(){
			$http.get(searchUrl,{params:{"searchText":$scope.data.searchText,"offSet":$scope.data.offset,"resultsPerPage":$scope.data.resultsPerPage}}).then(function(response) {
				console.log(response.data);
				$scope.data.assetObjects = response.data.assetsArray;
				$scope.data.offset = response.data.offset;
				$scope.data.hitsPerPage = response.data.hitsPerPage;
				$scope.data.totalMatches = response.data.totalMatches;
			})};
	}]);
