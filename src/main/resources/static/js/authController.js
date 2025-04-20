angular.module('ufcManager', ['ngStorage'])
  .controller('authController', function($scope, $http, $localStorage, authService) {

    $scope.user = {};

    authService.initUser($scope);

    $scope.logout = function () {
      authService.clearUser($scope);
      window.location.href = 'login.html';
    };



    $scope.login = function () {
      $http.post('/api/users/login', $scope.user)
        .then(function (response) {
          $localStorage.ufcUser = response.data;
          $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
          window.location.href = 'index.html';
        })
        .catch(function (error) {
          alert('Login failed: ' + (error.data?.message || 'Unknown error'));
        });
    };



    $scope.registerMe = function () {
      if ($scope.user.password !== $scope.user.confirmPassword) {
        alert('Passwords do not match!');
        return;
      }
      const payload = {
        username: $scope.user.username,
        password: $scope.user.password
      };
      $http.post('/api/users/register', payload)
        .then(function () {
          alert('Registration successful!');
          window.location.href = 'login.html';
        })
        .catch(function (error) {
          alert('Registration failed: ' + (error.data?.message || 'Unknown error'));
        });
    };

  });
