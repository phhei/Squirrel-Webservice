app.controller('homeCtrl', function($scope, $http) {
    $scope.getData = function(){
        $http({
            method : "GET",
            url : "/data/urls.json"
        }).then(function mySuccess(response) {
            $scope.pendingURIs = response.data.urIs.pendingURIs;
            $scope.nextCrawledURIs = response.data.urIs.nextCrawledURIs;
            $scope.countOfWorker = response.data.urIs.countOfWorker;
            $scope.countOfDeadWorker = response.data.urIs.countOfDeadWorker;
            $scope.runtimeInSeconds = response.data.urIs.runtimeInSeconds;
            $scope.writeTime = response.data.urIs.writeTime;
            $scope.readTime = response.data.urIs.readTime;
            $scope.ipStringListMap = response.data.urIs.ipStringListMap;
            //console.log($scope.countOfWorker);
        }, function myError(response) {
            console.log(response);
        });
    }
    //Refreshed data after 10 seconds
    setInterval($scope.getData, 50000);
});