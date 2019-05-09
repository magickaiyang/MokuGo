let beginLogin = function () {
    let ui = new firebaseui.auth.AuthUI(firebase.auth());
    let uiConfig = {
        callbacks: {
            signInSuccessWithAuthResult: function (authResult, redirectUrl) {
                if (authResult.user) {
                    console.log(authResult.user.displayName, 'logged in');

                    let indicator = document.getElementById('signin-indicator');
                    indicator.textContent='Processing your request...';
                    indicator.hidden=false;
                }
                return false;   //does not redirect
            },
        },
        // Will use popup for IDP Providers sign-in flow instead of the default, redirect.
        signInFlow: 'redirect',
        signInOptions: [
            {
                provider: firebase.auth.GoogleAuthProvider.PROVIDER_ID,
                authMethod: 'https://accounts.google.com',
                clientId: '926330483640-m1i1nn6qid5mivdkc731everjmhm3o1d.apps.googleusercontent.com'
            },
        ],
        credentialHelper: firebaseui.auth.CredentialHelper.GOOGLE_YOLO
    };
    ui.start('#firebaseui-auth-container', uiConfig);

    // username = document.getElementById("username").value;
    // window.location.href = '/index?username=' + username;
};
