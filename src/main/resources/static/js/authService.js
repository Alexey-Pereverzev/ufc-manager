// authService.js
angular.module('ufcManager')
  .factory('authService', function($localStorage, $http) {
    return {

      isTokenExpired: function(token) {
        try {
          const payload = JSON.parse(atob(token.split('.')[1]));
          const currentTime = Math.floor(Date.now() / 1000);
          return currentTime > payload.exp;
        } catch (e) {
          console.error("Token parse error:", e);
          return true;
        }
      },

      clearUser: function(scope) {
        delete $localStorage.ufcUser;
        $http.defaults.headers.common.Authorization = '';
        scope.username = 'Guest';
        scope.role = 'GUEST';
      },

      initUser: function(scope) {
        const user = $localStorage.ufcUser;
        if (user && user.token) {
          if (!this.isTokenExpired(user.token)) {
            $http.defaults.headers.common.Authorization = 'Bearer ' + user.token;
            scope.username = user.username;
            scope.role = user.role;
            return;
          } else {
            console.warn("Token expired, clearing user.");
            this.clearUser(scope);
            return;
          }
        }
        scope.username = 'Guest';
        scope.role = 'GUEST';
      },

      saveUser: function(user, scope) {
        $localStorage.ufcUser = user;
        $http.defaults.headers.common.Authorization = 'Bearer ' + user.token;
        scope.username = user.username;
        scope.role = user.role;
      }
    };
  });
