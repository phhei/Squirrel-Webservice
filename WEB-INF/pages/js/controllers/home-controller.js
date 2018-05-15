app.controller('homeCtrl', ['$scope', '$http', function($scope, $http) {

  $scope.autoUpdate = true;
  $scope.searchQuery = 'query';

  $scope.searchQuery = function () {
      $http({
          method : "GET",
          url : "/observer/inputValue"
      }).then(function mySuccess(response) {
          $scope.totalURIS = response.data.totalURIS;
          $scope.runtimeInSeconds = response.data.runtimeInSeconds;
          $scope.readTime = response.data.readTime;
          $scope.writeTime = response.data.writeTime;
          $scope.ipStringListMap = response.data.ipStringListMap;

      }, function myError(response) {
          console.log(response);
      });
  }


  $scope.getData = function() {
    if ($scope.autoUpdate) {
      $http({
        method : "GET",
        url : "/observer"
      }).then(function mySuccess(response) {
        $scope.pendingURIs = response.data.countOfPendingURIs;
        $scope.countOfCrawledURIs = response.data.countOfCrawledURIs;
        $scope.countOfWorker = response.data.countOfWorker;
        $scope.countOfDeadWorker = response.data.countOfDeadWorker;
        $scope.runtimeInSeconds = response.data.runtimeInSeconds;
        $scope.writeTime = response.data.writeTime;
        $scope.readTime = response.data.readTime;
        $scope.ipStringListMap = response.data.ipStringListMap;
        $scope.PendingURIS = response.data.pendingURIs;
        $scope.NextCrawledURIs = response.data.nextCrawledURIs;
      }, function myError(response) {
        console.log(response);
      });
    }
  };

    $scope.get1 = function() {
        console.log("get1 called.");
    };

    $scope.get1();
    $scope.searchQuery();
    $scope.getData();


  setInterval($scope.getData, 5000);
}]);