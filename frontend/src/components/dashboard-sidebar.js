import { useEffect } from 'react';
import NextLink from 'next/link';
import { useRouter } from 'next/router';
import PropTypes from 'prop-types';
import {
  Avatar,
  Box,
  Button,
  Divider,
  Drawer,
  List, ListItem, ListItemAvatar, ListItemText,
  Typography,
  useMediaQuery
} from '@mui/material';
import { ChartBar as ChartBarIcon } from '../icons/chart-bar';
import { Cog as CogIcon } from '../icons/cog';
import { Lock as LockIcon } from '../icons/lock';
import { Selector as SelectorIcon } from '../icons/selector';
import { ShoppingBag as ShoppingBagIcon } from '../icons/shopping-bag';
import { User as UserIcon } from '../icons/user';
import { UserAdd as UserAddIcon } from '../icons/user-add';
import { Users as UsersIcon } from '../icons/users';
import { XCircle as XCircleIcon } from '../icons/x-circle';
import LogoutIcon from '@mui/icons-material/Logout';
import { NavItem } from './nav-item';
import { useAuth } from '../contexts/auth';

const items = [
  {
    href: '/',
    icon: (<ChartBarIcon fontSize="small"/>),
    title: 'Panou de control'
  },
  {
    href: '/users',
    icon: (<UsersIcon fontSize="small"/>),
    title: 'Utilizatori'
  },
  {
    href: '/tickets',
    icon: (<ShoppingBagIcon fontSize="small"/>),
    title: 'Tichete'
  },
  {
    href: '/account',
    icon: (<UserIcon fontSize="small"/>),
    title: 'Contul meu'
  },
  {
    href: '/settings',
    icon: (<CogIcon fontSize="small"/>),
    title: 'Settings'
  },
  {
    href: '/login',
    icon: (<LockIcon fontSize="small"/>),
    title: 'Login'
  },
  {
    href: '/register',
    icon: (<UserAddIcon fontSize="small"/>),
    title: 'Register'
  },
  {
    href: '/404',
    icon: (<XCircleIcon fontSize="small"/>),
    title: 'Error'
  }
];

export const DashboardSidebar = (props) => {
  const { open, onClose } = props;
  const router = useRouter();
  const {logout} = useAuth();
  const lgUp = useMediaQuery((theme) => theme.breakpoints.up('lg'), {
    defaultMatches: true,
    noSsr: false
  });

  useEffect(
    () => {
      if (!router.isReady) {
        return;
      }

      if (open) {
        onClose?.();
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [router.asPath]
  );


  const content = (
    <>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          height: '100%'
        }}
      >
        <div>
          <Box sx={{ p: 0 }}>
            <NextLink
              href="/"
              passHref
            >
                <List sx={{paddingLeft: '10px'}}>
                  <ListItem>
                    <ListItemAvatar>
                      <Avatar
                        alt="Logo"
                        src={'/static/logo.svg'}
                        variant="square"
                      />
                    </ListItemAvatar>
                    <ListItemText primary={<Typography
                      color="inherit"
                      variant="subtitle1"
                    >
                      OS Ticket
                    </Typography>} />
                  </ListItem>
                </List>
            </NextLink>
          </Box>
        </div>
        <Divider
          sx={{
            borderColor: '#2D3748',
            mt: 0,
            mb: 3
          }}
        />
        <Box sx={{ flexGrow: 1 }}>
          {items.map((item) => (
            <NavItem
              key={item.title}
              icon={item.icon}
              href={item.href}
              title={item.title}
            />
          ))}
        </Box>
        <Divider sx={{ borderColor: '#2D3748' }}/>
        <Box
          sx={{
            px: 2,
            py: 3
          }}
        >
          <Typography
            color="neutral.100"
            variant="subtitle2"
          >
            Sistem de ticketing
          </Typography>
          <Typography
            color="neutral.500"
            variant="body2"
          >
            Dezvoltat de Webitech @{new Date().getFullYear()}
          </Typography>
          <Box
            sx={{
              display: 'flex',
              mt: 2,
              mx: 'auto',
              width: '160px',
              '& img': {
                width: '100%'
              }
            }}
          >
            <img
              alt="Webitech Dev"
              src="/static/images/sidebar_pro.png"
            />
          </Box>
            <Button
              color="error"
              onClick={logout}
              component="a"
              endIcon={(<LogoutIcon/>)}
              fullWidth
              sx={{ mt: 2 }}
              variant="contained"
            >
              Deconectare
            </Button>
        </Box>
      </Box>
    </>
  );

  if (lgUp) {
    return (
      <Drawer
        anchor="left"
        open
        PaperProps={{
          sx: {
            backgroundColor: 'neutral.900',
            color: '#FFFFFF',
            width: 280
          }
        }}
        variant="permanent"
      >
        {content}
      </Drawer>
    );
  }

  return (
    <Drawer
      anchor="left"
      onClose={onClose}
      open={open}
      PaperProps={{
        sx: {
          backgroundColor: 'neutral.900',
          color: '#FFFFFF',
          width: 280
        }
      }}
      sx={{ zIndex: (theme) => theme.zIndex.appBar + 100 }}
      variant="temporary"
    >
      {content}
    </Drawer>
  );
};

DashboardSidebar.propTypes = {
  onClose: PropTypes.func,
  open: PropTypes.bool
};
