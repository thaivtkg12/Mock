$().ready(function() {
	$("#contactform").validate({
		rules: {
			name: {
				required: true
			},
			email: {
				required: true,
				email: true
			},
			message: {
				required: true
			},
			subject:{
				required:true
			}
		},
		messages: {
			name: "Enter your name",
			email: {
				required: "Please enter a email",
				email: "Please enter a valid email"
			},
			message: {
				required: "Please enter a message"
			},
			subject:"Please enter subject"
		}
	})
})