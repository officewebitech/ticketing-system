import { createContext, useContext, useEffect, useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/router';

const AuthContext = createContext({});

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loginError, setLoginError] = useState('');
  const [registerError, setRegisterError] = useState('');
  const [confirmError, setConfirmError] = useState('');
  const [confirmMessage, setConfirmMessage] = useState('');
  const route = useRouter();
  const API_URL = 'http://localhost:8080';

  const login = async (email, password) => {

    await axios.post(API_URL + '/api/login', { email, password })
               .then(response => {
                 if (response.data.token) {
                   sessionStorage.setItem('user', JSON.stringify(response.data));
                   setUser(response.data);
                   const returnUrl = route.query.returnUrl || '/';
                   route.push(returnUrl);
                   setLoginError('');
                 }
                 return response.data;
               }).catch((error) => {
        setLoginError(error.response.data.message);

      });
  };

  const register = async (registerUser) => {
    await axios.post(API_URL + '/api/register', registerUser)
               .then(response => {
                 if (response.status === 200) {
                   setRegisterError('');
                   route.push('/confirm-account');
                 }
               }).catch((error) => {
        setRegisterError(error.response.data.message);
      });
  };

  const confirmAccount = async (token) => {
    await axios.get(API_URL + '/api/register/confirm', { params: { token: token } })
               .then(response => {
                 if (response.status === 200) {
                   setConfirmError('');
                   setConfirmMessage(response.data.message);
                   setTimeout(() => {
                     route.push('/login');
                     setConfirmMessage('');
                   }, 3000);
                 }
               }).catch((error) => {
            setConfirmError(error.response.data.message);
      });
  };

  const logout = () => {
    sessionStorage.clear();
    route.push({
      pathname: '/login'
    });
  };

  return (
    <AuthContext.Provider value={{
      isAuthenticated: user,
      user,
      login,
      register,
      confirmAccount,
      logout,
      confirmMessage,
      loginError,
      registerError,
      confirmError
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

export const ProtectRoute = ({ children }) => {
  const router = useRouter();
  const [authorized, setAuthorized] = useState(false);
  const { user } = useAuth();

  useEffect(() => {
    // on initial load - run auth check
    authCheck(router.asPath);

    // on route change start - hide page content by setting authorized to false
    const hideContent = () => setAuthorized(false);
    router.events.on('routeChangeStart', hideContent);

    // on route change complete - run auth check
    router.events.on('routeChangeComplete', authCheck);

    // unsubscribe from events in useEffect return function
    return () => {
      router.events.off('routeChangeStart', hideContent);
      router.events.off('routeChangeComplete', authCheck);
    };

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [user]);

  const authCheck = async (url) => {
    const publicPaths = ['/login', '/register', '/confirm-account'];
    const path = url.split('?')[0];
    if (!sessionStorage.getItem('user') && !publicPaths.includes(path)) {
      setAuthorized(false);
      await router.push({
        pathname: '/login'
      });
    }
    if (sessionStorage.getItem('user') && publicPaths.includes(path)) {
      await router.push({
        pathname: '/'
      });
    } else {
      setAuthorized(true);
    }
  };

  return (authorized && children);

};
