(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);throw new Error("Cannot find module '"+o+"'")}var f=n[o]={exports:{}};t[o][0].call(f.exports,function(e){var n=t[o][1][e];return s(n?n:e)},f,f.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){

module.exports = function (app) {
    app.controller('LoginController', ['$scope', '$http', '$location', function ($scope, $http, $location) {
        $scope.name = '';

        $scope.login = function () {
            console.log(`Logging ${$scope.name} in as we speak`);

            $http({
                url: '/users',
                method: 'post',
                data: {
                    name: $scope.name,
                    password: 'password123',
                },
            }).then(function () {
                $location.path('/tomalikes');
            }).catch(function () {
                console.error('INTRUDER');
            });
        };
    }]);
}
},{}],2:[function(require,module,exports){

module.exports = function (app) {
    app.controller('TomALikeController', ['$scope', '$http', 'TomALikeService', function ($scope, $http, TomALikeService) {
        $scope.tomalikes = TomALikeService.getTomALikes();

        $scope.fav = function (tom, looksLike) {
            $http({
                url: '/favs',
                method: 'post',
                data: {
                    tomId: tom.id,
                    looksLikeTom: looksLike,
                }
            });
        };
    }]);
};
},{}],3:[function(require,module,exports){
let app = angular.module('TomALikeApp', ['ngRoute']);

require('./services/tomalike')(app);
require('./controllers/login')(app);
require('./controllers/tomalike')(app);

app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/', {
            controller: 'LoginController',
            templateUrl: 'templates/login.html',
        })
        .when('/tomalikes', {
            controller: 'TomALikeController',
            templateUrl: 'templates/tomalike.html',
        })
        .when('/upload', {
            templateUrl: 'templates/upload.html',
        });
}])
},{"./controllers/login":1,"./controllers/tomalike":2,"./services/tomalike":4}],4:[function(require,module,exports){
module.exports = function (app) {
    app.factory('TomALikeService', ['$http', function ($http) {
        let tomalikes = [];

        return {
            getTomALikes: function () {
                $http({
                    url: '/tomalikes',
                    method: 'get'
                }).then(function (results) {
                    angular.copy(results.data, tomalikes)
                });

                return tomalikes;
            },
        };
    }]);
};
},{}]},{},[3])