import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Login from "../pages/Login";
import Home from "../pages/Home";
import MyProfile from "../pages/MyProfile";
import MyProject from "../pages/MyProject";
import MyProjectVersion2 from "../pages/MyProjectVersion2";
import MyTask from "../pages/MyTask";
import Logout from "../pages/Logout";
import GenUser from "../pages/GenUser";
import GenProject from "../pages/GenProject";
import Notification from "../pages/Notification";
import ProjectDetail from "../pages/ProjectDetail";
import MemberProject from "../pages/MemberProject";
import TasksInProject from "../pages/TasksInProject";
import Profile from "../pages/Profile";
import ReportProject from "../pages/ReportProject";
import {SnackbarProvider} from "../context/SnackbarContext";
import Register from "../pages/Register";
import TaskDetail from "../pages/TaskDetail";
import AdminPage from "../pages/AdminPage";

const AppRoutes = () => {
  return (
    <SnackbarProvider>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/logout" element={<Logout />} />
          <Route path="/" element={<Home />} />

          <Route path="/profile" element={<MyProfile />} />
          <Route path="/profile/:userId" element={<Profile />} />

          <Route path="/my-project" element={<MyProject />} />
          <Route path="/my-task" element={<MyTask />} />
          <Route path="/notifications" element={<Notification />} />

          <Route path="/project/:projectId" element={<ProjectDetail />} />
          <Route path="/project/:projectId/member" element={<MemberProject />} />
          <Route path="/project/:projectId/task" element={<TasksInProject />} />
          <Route path="/project/:projectId/report" element={<ReportProject />} />

          <Route path="/task/:taskId" element={<TaskDetail />} />

          <Route path="/admin" element={<AdminPage />} />

          <Route path="/gen-user" element={<GenUser />} />
          <Route path="/gen-project" element={<GenProject />} />
          <Route path="/test" element={<MyProjectVersion2 />} />
        </Routes>
      </Router>
    </SnackbarProvider>
  );
};

export default AppRoutes;
