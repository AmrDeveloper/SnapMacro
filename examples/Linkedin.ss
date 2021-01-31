func connectButton {
  #point to connect button
  mouse point 514 543
  mouse click left
}

func closeButton {
  #point to close button
  mouse point 579 307
  mouse click left
}

repeat(3) {
   connectButton()
   delay 2000
   closeButton()
   delay 2000
}
