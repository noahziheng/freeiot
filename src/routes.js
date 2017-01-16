import Hello from './pages/Hello'
import NotFound from './pages/NotFound'
import Login from './pages/Login'
import Reg from './pages/Reg'

const routes = [
  { path: '/dashboard', component: Hello },
  { path: '/login', component: Login },
  { path: '/reg', component: Reg },
  { path: '*', component: NotFound }
]
export default routes
