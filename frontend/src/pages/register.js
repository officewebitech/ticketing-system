import Head from 'next/head';
import NextLink from 'next/link';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import {
  Box,
  Button,
  Checkbox,
  Container,
  FormHelperText,
  Link,
  TextField,
  Typography
} from '@mui/material';
import { useAuth } from '../contexts/auth';
import Alert from '@mui/material/Alert';
import AlertTitle from '@mui/material/AlertTitle';

const Register = () => {
  const { register, registerError } = useAuth();
  const formik = useFormik({
    initialValues: {
      email: '',
      firstName: '',
      lastName: '',
      password: '',
      confirmPassword: '',
      phoneNumber: '',
      projectName: '',
      policy: false
    },
    validationSchema: Yup.object({
      email: Yup
        .string()
        .email(
          'Adresa de email nu este valida !')
        .max(255)
        .required(
          'Campul este obligatoriu !'),
      firstName: Yup
        .string()
        .max(255)
        .required(
          'Campul este obligatoriu !'),
      lastName: Yup
        .string()
        .max(255)
        .required(
          'Campul este obligatoriu !'),
      password: Yup
        .string()
        .max(255)
        .required(
          'Campul este obligatoriu !'),
      confirmPassword: Yup
        .string()
        .max(255)
        .required(
          'Campul este obligatoriu !'
        ),
      phoneNumber: Yup
        .string()
        .max(15)
        .required(
          'Campul este obligatoriu !'
        ),
      projectName: Yup
        .string()
        .max(150)
        .required(
          'Campul este obligatoriu !'
        ),
      policy: Yup
        .boolean()
        .oneOf(
          [true],
          'Accepta termeni si conditii !'
        )
    }),
    onSubmit: () => {
      register(formik.values);
    }
  });

  return (
    <>
      <Head>
        <title>
          Inregistrare | OS Ticket
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
            <Box sx={{ my: 3 }}>
              <Typography
                color="textPrimary"
                variant="h4"
              >
                Inregistreaza un nou cont
              </Typography>
              <Typography
                color="textSecondary"
                gutterBottom
                variant="body2"
              >
                Foloseste adresa de email pentru a crea un cont nou.
              </Typography>
              {registerError ? (<Alert severity="error">
                <AlertTitle>{registerError}</AlertTitle>
              </Alert>) : <Typography/>}
            </Box>
            <TextField
              error={Boolean(formik.touched.lastName && formik.errors.lastName)}
              fullWidth
              helperText={formik.touched.lastName && formik.errors.lastName}
              label="Nume"
              margin="normal"
              name="lastName"
              onBlur={formik.handleBlur}
              onChange={formik.handleChange}
              value={formik.values.lastName}
              variant="outlined"
            />

            <TextField
              error={Boolean(formik.touched.firstName && formik.errors.firstName)}
              fullWidth
              helperText={formik.touched.firstName && formik.errors.firstName}
              label="Prenume"
              margin="normal"
              name="firstName"
              onBlur={formik.handleBlur}
              onChange={formik.handleChange}
              value={formik.values.firstName}
              variant="outlined"
            />

            <TextField
              error={Boolean(formik.touched.email && formik.errors.email)}
              fullWidth
              helperText={formik.touched.email && formik.errors.email}
              label="Adresa de mail"
              margin="normal"
              name="email"
              onBlur={formik.handleBlur}
              onChange={formik.handleChange}
              type="email"
              value={formik.values.email}
              variant="outlined"
            />

            <TextField
              error={Boolean(formik.touched.phoneNumber && formik.errors.phoneNumber)}
              fullWidth
              helperText={formik.touched.phoneNumber && formik.errors.phoneNumber}
              label="Numar de telefon"
              margin="normal"
              name="phoneNumber"
              onBlur={formik.handleBlur}
              onChange={formik.handleChange}
              type="text"
              value={formik.values.phoneNumber}
              variant="outlined"
            />

            <TextField
              error={Boolean(formik.touched.projectName && formik.errors.projectName)}
              fullWidth
              helperText={formik.touched.projectName && formik.errors.projectName}
              label="Nume proiect"
              margin="normal"
              name="projectName"
              onBlur={formik.handleBlur}
              onChange={formik.handleChange}
              type="text"
              value={formik.values.projectName}
              variant="outlined"
            />

            <TextField
              error={Boolean(formik.touched.password && formik.errors.password)}
              fullWidth
              helperText={formik.touched.password && formik.errors.password}
              label="Parola"
              margin="normal"
              name="password"
              onBlur={formik.handleBlur}
              onChange={formik.handleChange}
              type="password"
              value={formik.values.password}
              variant="outlined"
            />

            <TextField
              error={Boolean(formik.touched.confirmPassword && formik.errors.confirmPassword)}
              fullWidth
              helperText={formik.touched.confirmPassword && formik.errors.confirmPassword}
              label="Confirmare parola"
              margin="normal"
              name="confirmPassword"
              onBlur={formik.handleBlur}
              onChange={formik.handleChange}
              type="password"
              value={formik.values.confirmPassword}
              variant="outlined"
            />

            <Box
              sx={{
                alignItems: 'center',
                display: 'flex',
                ml: -1
              }}
            >
              <Checkbox
                checked={formik.values.policy}
                name="policy"
                onChange={formik.handleChange}
              />
              <Typography
                color="textSecondary"
                variant="body2"
              >
                Am citit
                {' '}
                <NextLink
                  href="#"
                  passHref
                >
                  <Link
                    color="primary"
                    underline="always"
                    variant="subtitle2"
                  >
                    Termeni si conditii
                  </Link>
                </NextLink>
              </Typography>
            </Box>
            {Boolean(formik.touched.policy && formik.errors.policy) && (
              <FormHelperText error>
                {formik.errors.policy}
              </FormHelperText>
            )}
            <Box sx={{ py: 2 }}>
              <Button
                color="primary"
                disabled={registerError ? !formik.isSubmitting : formik.isSubmitting}
                fullWidth
                size="large"
                type="submit"
                variant="contained"
              >
                {formik.isSubmitting ? 'Contul se inregistreaza...' : 'Inregistreaza-te'}
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
};

export default Register;
