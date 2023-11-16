; #NoTrayIcon

SetCapsLockState, AlwaysOff

; Ctrl::Alt
; Alt::Ctrl
^q::WinClose, A

#If GetKeyState("Capslock","P")
	i::Up
	j::Left
	k::Down
	l::Right
	u::BackSpace
	o::Del

	w::Up
	a::Left
	s::Down
	d::Right
	e::BackSpace
	q::Enter
	t::_
	z::Home
	x::End
	c::Esc
	h::=
	p::+

	n::Home
	m::End

#If

^F10::Send CtrlUp