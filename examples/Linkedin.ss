function connectButton {
  #point to connect button
  mouse point 514 543
  mouse click left
}

function closeButton {
  #point to close button
  mouse point 579 307
  mouse click left
}

repeat(10) {
   connectButton()
   sleep 2000
   closeButton()
   sleep 2000
}