angular.module('ufcManager')
  .controller('eventController', function($scope, $http, authService) {

    authService.initUser($scope);

    $scope.logout = function () {
      authService.clearUser($scope);
      window.location.href = 'login.html';
    };

    $scope.mode = 'list';  // list | edit | fightEdit | view

    $scope.viewTournament = t => {
      $http.get(`/api/tournaments/${t.id}`)
        .then(response => {
          $scope.tournament = response.data;
          $scope.mode = 'view';
        })
        .catch(error => console.error("Error loading tournament:", error));
    };

    $scope.isUnauthorized = () => authService.isUnauthorized();
    $scope.isGuest        = () => $scope.role === 'GUEST';
    $scope.isManager      = () => $scope.role === 'MANAGER';
    $scope.isAdmin        = () => $scope.role === 'ADMIN';


    $http.get('/api/tournaments')
      .then(response => {
        $scope.tournaments = response.data.sort((a, b) => new Date(b.date) - new Date(a.date));
      })
      .catch(error => {
        console.error("Error loading tournaments:", error);
      });

    $scope.weightClasses = [];

    $http.get('/api/weight-classes')
      .then(response => {
        $scope.weightClasses = response.data;
      })
      .catch(err => console.error("Error loading weight classes:", err));


    /* ---------- helpers ---------- */
    const toDateObj = str => new Date(str + 'T00:00:00');          // -> Date (лок.)
    const toYmdStr  = d   => {                                     // -> 'yyyy-MM-dd'
        const pad = n => ('0'+n).slice(-2);
        return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}`;
    };

    $scope.fight = {};

    $scope.addTournament = () => {
      $scope.tournament = { date: new Date(), fights: [] };      // сразу Date‑obj
      $scope.mode = 'edit';
    };

    $scope.editTournament = t => {
      $scope.tournament = angular.copy(t);
      $scope.tournament.date = toDateObj(t.date);                // ← Date‑obj
      $scope.mode = 'edit';
    };

    $scope.saveTournament = () => {
      const payload = {
        ...$scope.tournament,
        date: toYmdStr($scope.tournament.date)
      };
      if ($scope.tournament.id) {
        $http.put(`/api/tournaments/${$scope.tournament.id}`, payload)
          .then(() => $scope.cancelEdit())
          .catch(err => console.error("Error updating tournament:", err));
      } else {
        $http.post('/api/tournaments', payload)
          .then(response => {
            $scope.tournaments.push(response.data);
            $scope.cancelEdit();
          })
          .catch(err => console.error("Error creating tournament:", err));
      }
    };

    $scope.cancelEdit = () => {
      $scope.tournament = {};
      $scope.fight = {};
      $scope.mode = 'list';
    };

    $scope.goToFightEdit = () => {
        $scope.mode = 'fightEdit';
    };


    // Fight card methods
    $scope.addFight = () => {
      $scope.fight = {
        fighter1: '',
        fighter2: '',
        result: '',
        weightClass: $scope.weightClasses[0]
      };
      $scope.fightToEdit = false;
      $scope.mode = 'fightEdit';
    };

    $scope.editFight = fight => {
      $scope.fight = angular.copy(fight);
      $scope.fightToEdit = true;
      $scope.mode = 'fightEdit';
    };

    $scope.saveFight = () => {
      const fight = angular.copy($scope.fight);
      const url = `/api/tournaments/${$scope.tournament.id}/fights`;
      if ($scope.fightToEdit && fight.id) {
        // === UPDATE AN EXISTING FIGHT ===
        $http.put(`${url}/${fight.id}`, fight)
          .then(response => {
            // Update the fight in the list (by id)
            const idx = $scope.tournament.fights.findIndex(f => f.id === response.data.id);
            if (idx !== -1) {
              $scope.tournament.fights[idx] = response.data;
            }
            $scope.fight = {};
            $scope.fightToEdit = false;
            $scope.mode = 'edit';
          })
          .catch(err => console.error("Error updating fight:", err));
      } else {
        // === CREATE A NEW FIGHT ===
        $http.post(url, fight)
          .then(response => {
            $scope.tournament.fights.push(response.data);
            $scope.fight = {};
            $scope.fightToEdit = false;
            $scope.mode = 'edit';
          })
          .catch(err => console.error("Error creating fight:", err));
      }
    };

    $scope.deleteFight = (fight) => {
      if (confirm(`Delete fight: ${fight.fighter1} ${fight.result} ${fight.fighter2}?`)) {
        $http.delete(`/api/tournaments/${$scope.tournament.id}/fights/${fight.id}`)
                 .then(() => {
                   $scope.tournament.fights = $scope.tournament.fights.filter(f => f.id !== fight.id);
                 })
                 .catch(err => console.error("Error deleting fight:", err));
      }
    };

    $scope.cancelFightEdit = () => {
      $scope.fight = {};
      $scope.fightToEdit = false;
      $scope.mode = 'edit';
    };

    $scope.refreshTournament = () => {
      $http.get(`/api/tournaments/${$scope.tournament.id}`)
        .then(response => $scope.tournament = response.data)
        .catch(err => console.error("Error refreshing tournament:", err));
    };


  });






//
//    $scope.resultTypes = ['def.', 'vs.'];
//    $scope.username  = 'john_doe';        // или ''
//    $scope.role      = 'MANAGER';         // GUEST | MANAGER | ADMIN
//
//    // MOCK tournaments
//    $scope.tournaments = [
//      {
//        id: 1,
//        name: 'UFC 300',
//        date: '2025-06-30',
//        location: 'Las Vegas',
//        fights: [
//          {
//            id: 1,
//            fighter1: 'Jon Jones',
//            result: 'def.',
//            fighter2: 'Stipe Miocic',
//            weightClass: 'Heavyweight'
//          },
//          {
//            id: 2,
//            fighter1: 'Charles Oliveira',
//            result: 'vs.',
//            fighter2: 'Islam Makhachev',
//            weightClass: 'Lightweight'
//          }
//        ]
//      },
//      {
//        id: 2,
//        name: 'UFC 299',
//        date: '2025-06-01',
//        location: 'Miami',
//        fights: [
//          {
//            id: 3,
//            fighter1: 'Sean O\'Malley',
//            result: 'def.',
//            fighter2: 'Petr Yan',
//            weightClass: 'Bantamweight'
//          }
//        ]
//      }
//    ];