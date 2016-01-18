package com.benlinus92.synchronize.validator;

import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.benlinus92.synchronize.model.Profile;

public class EditedProfileValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		return Profile.class.equals(arg0);
	}

	@Override
	public void validate(Object obj, Errors e) {
		Profile user = (Profile) obj;
		if(!user.getPassword().equals(user.getPasswordConfirm()))
			e.rejectValue("passwordConfirm", "NotEqual.user.passwordConfirm");
		EmailValidator eVal = new EmailValidator();
		if(!eVal.isValid(user.getEmail(), null))
			e.rejectValue("email", "Email.user.email");
	}

}
