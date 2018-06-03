CREATE PROCEDURE test_out_param(OUT outparam INT, inparam INT)
   BEGIN ATOMIC
	SET outparam = inparam * 100 + 11; 
   END
