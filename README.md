# JodexRestAPI
[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/H2H8GLWZE)

## /api/server
```JSON
{
  "serverOnline":"1",
  "playerList":"_Jodex__",
  "serverUptime":"1h 27s", 
  "worldTimeStatus":"Night",
  "serverTPS":"*20.0"
}
```
## /api/player/NICKNAME
```JSON
{
  "kills":"0",
  "timeDays":"0",
  "timeSeconds":"51",
  "balance":"0.0",
  "mobKills":"2",
  "success":true,
  "timeHours":"6",
  "deaths":"1",
  "timeMinutes":"43"
}

```
## NodeJS examples
PlayerRequest (node-fetch)

```js
  let url = `http://localhost:8080/api/player/NICKNAME`
  let response;
  try {
  response = await fetch(url, { method: "Get" });
  } catch (err) {
    return console.log('Response not found');
  }
  const json = await response.json();
```

PostRequest (node-fetch)
```js
  let url = `http://localhost:8080/api/server/command`
  let response;
  const params = new URLSearchParams();
  params.append('command', "test command");
  try {
     response = await fetch(url, {method: 'POST', body: params});
    } catch (err) {
    return console.log('Response not found');
  }
```
