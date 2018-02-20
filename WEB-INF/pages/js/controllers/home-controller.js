app.controller('homeCtrl', ['$scope', '$http', function($scope, $http) {
  
  $scope.autoUpdate = true;

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

  $scope.getData();

  setInterval($scope.getData, 5000);
}]);