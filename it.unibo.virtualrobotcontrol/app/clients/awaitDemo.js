/*
awaitDemo.js
*/
function delay(x, time) {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve(x);
    }, time);
  });
}

async function f1() {
  var x = await delay(10, 1000);
  console.log(x); // 10 (after 1 sec)
}

f1();