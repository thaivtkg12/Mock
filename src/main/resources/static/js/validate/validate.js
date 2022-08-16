$().ready(function() {
	$("#productform").validate({
		rules:{
			name:{
				required:true,
			},
			price:{
				required:true,
				min:1
			},
			quantity:{
				required:true,
				min:1
			},
			enteredDate:{
				required:true
			},
			discount:{
				required:true,
				min:0
			}
		},
		messages:{
			name:{
				required:"Please enter name of product",
				},
			price:{
				required:"Please enter product price",
				min:"Price must greater or equal 1"
			},
			quantity:{
				required:"Please enter quantity",
				min:"Quantiy must greater or equal 1"			
			},
			enteredDate:{
				required:"Please select enteredDate"
			},
			discount:{
				required:"Please enter a discount",
				min:"Discount must greater or equal zero"
			}
		}
	})
	$("#updateform").validate({
		rules:{
			name:{
				required:true,
			},
			price:{
				required:true,
				min:1
			},
			quantity:{
				required:true,
				min:1
			},
			enteredDate:{
				required:true
			},
			discount:{
				required:true,
				min:0
			}
		},
		messages:{
			name:{
				required:"Please enter name of product",
				},
			price:{
				required:"Please enter product price",
				min:"Price must greater or equal zero"
			},
			quantity:{
				required:"Please enter quantity",
				min:"Quantiy must greater or equal 1"			
			},
			enteredDate:{
				required:"Please select enteredDate"
			},
			discount:{
				required:"Please enter a discount",
				min:"Discount must greater or equal zero"
			}
		}
	})
});