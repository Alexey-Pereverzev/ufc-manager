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

      isUnauthorized: function() {
        const user = $localStorage.ufcUser;
        return !user || !user.token || this.isTokenExpired(user.token);
      },

      clearUser: function(scope) {
        delete $localStorage.ufcUser;
        $http.defaults.headers.common.Authorization = '';
        scope.username = '';
        scope.role = '';
      },

      initUser: function(scope) {
        const user = $localStorage.ufcUser;
        if (user && user.token && !this.isTokenExpired(user.token)) {
          $http.defaults.headers.common.Authorization = 'Bearer ' + user.token;
          scope.username = user.username;
          scope.role = user.role;
        } else {
          this.clearUser(scope); // убираем старые данные
        }
      },

      saveUser: function(user, scope) {
        $localStorage.ufcUser = user;
        $http.defaults.headers.common.Authorization = 'Bearer ' + user.token;
        scope.username = user.username;
        scope.role = user.role;
      }
    };
  });