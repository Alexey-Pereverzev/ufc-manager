angular.module('ufcManager')
  .controller('authController', function($scope, $http, $localStorage, authService) {

    $scope.user = {};

    authService.initUser($scope);

    $scope.logout = function () {
      authService.clearUser($scope);
      window.location.href = 'login.html';
    };



    $scope.login = function () {
      $http.post('/api/users/login', {
        username: $scope.user.username,
        password: $scope.user.password
      })
      .then(function (response) {
        const user = response.data;
        authService.saveUser(user, $scope);
        window.location.href = 'index.html'; // <<== вот это обязательно
      })
      .catch(function (error) {
        console.error("Login failed:", error);
        alert("Login failed. Please check your credentials.");
      });
    };

//          // -- LOGIN ------------------------------------------------------------
//          $scope.login = function () {
//            console.log('LOGIN submit', $scope.user);      // <- увидеть в консоли
//            $http.post('/api/users/login', {
//                username: $scope.user.username,
//                password: $scope.user.password
//            }).then(function (resp) {
//                authService.saveUser(resp.data, $scope);
//                window.location.href = 'index.html';
//            }).catch(function (err) {
//                console.error('Login failed', err);
//                alert('Login failed. Check credentials');
//            });
//          };



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
