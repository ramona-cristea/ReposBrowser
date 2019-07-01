# ReposBrowser
Sample app for browsing repositories on Github.

The application uses OAuth2 to get Github Authorization from user. After app was successfully authorized, the app provides the following functionalities: 
* Search funtion for repositories by simply entering a query;
* Filtering mechanism by programming language applied to results. 

## Getting Started

Clone the project by using git clone <repository_url> command or download it and extract it from the zip archive.

### Prerequisites

* Register the app for OAuth on Github (please see https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)
* Update variable from gradle.properties: GithubClientId = "YOUR_CLIENT_ID"
* Update variable from gradle.properties: GithubSecretId = "YOUR_CLIENT_SECRET"

YOUR_CLIENT_ID = The client ID you received from GitHub for your GitHub App.

YOUR_CLIENT_SECRET = The client secret you received from GitHub for your GitHub App.
