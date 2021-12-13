import Head from 'next/head';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { Box, Button, Container, Link, TextField, Typography } from '@mui/material';
import NextLink from 'next/link';
import { useAuth } from '../contexts/auth';
import Alert from '@mui/material/Alert';
import AlertTitle from '@mui/material/AlertTitle';
const ConfirmAccount = () => {
  const {confirmAccount, confirmError, confirmMessage} = useAuth();
  const formik = useFormik({
    initialValues: {
      token: '',
    },
    validationSchema: Yup.object({
      token: Yup
        .string()
        .max(255)
        .required('Campul este obligatoriu !'),
    }),
    onSubmit: () => {
      confirmAccount(formik.values.token);
      formik.resetForm();
    }
  });


  return(
    <>
      <Head>
        <title>
          Confirmare cont | OS Ticket
        </title>
      </Head>
      <Box
        component="main"
        sx={{
          alignItems: 'center',
          display: 'flex',
          flexGrow: 1,
          minHeight: '100%'
        }}
      >
        <Container maxWidth="sm">
          <form onSubmit={formik.handleSubmit}>
            <Box sx={{my: 3}}>
              <Typography color="textPrimary" variant="h4">
                Confirmare Cont
              </Typography>
              <Typography
                color="textSecondary"
                gutterBottom
                variant="body2"
              >
                Introdu in campul de mai jos codul de activare primit pe adresa de email.
              </Typography>
              {confirmError ? (<Alert severity="error">
                <AlertTitle>{confirmError}</AlertTitle>
              </Alert>) : <Typography />}

              {confirmMessage ? (<Alert severity="success">
                <AlertTitle>{confirmMessage}</AlertTitle>
              </Alert>) : <Typography />}
            </Box>
            <TextField
              error={Boolean(formik.touched.token && formik.errors.token)}
              fullWidth
              helperText={formik.touched.token && formik.errors.token}
              label="Cod de activare ***"
              margin="normal"
              name="token"
              type="text"
              onBlur={formik.handleBlur}
              onChange={formik.handleChange}
              value={formik.values.token}
              variant="outlined"
            />
            <Box sx={{ py: 2 }}>
              <Button
                color="primary"
                disabled={confirmMessage ? !formik.isSubmitting : formik.isSubmitting}
                fullWidth
                size="large"
                type="submit"
                variant="contained"
              >
                Activare cont
              </Button>
            </Box>
            <Typography
              color="textSecondary"
              variant="body2"
            >
              Ai cont ?
              {' '}
              <NextLink
                href="/login"
                passHref
              >
                <Link
                  variant="subtitle2"
                  underline="hover"
                >
                  Autentificare
                </Link>
              </NextLink>
            </Typography>
          </form>
        </Container>

      </Box>

    </>
  );

}

export default ConfirmAccount;
