import React, {useEffect, useRef, useState} from "react";
import {Box, Menu} from "@mui/material";
import ReusableTable from "../components/ReusableTable";
import Scene from "./Scene";
import {isAuthenticated, logOut} from "../services/authenticationService";
import {useLoadUserInfo} from "../services/userService";
import {useNavigate} from "react-router-dom";
import {handleDate} from "../services/dateTimeService";
import {changeTaskStatus, getMyTasks, updateTask} from "../services/taskService";
import {getProjectById} from "../services/projectService";
import TitleTypography from "../components/TitleTypography";
import CustomMenuItem from "../components/CustumMenuItem";
import {useSnackbar} from "../context/SnackbarContext";
import {getMyProfile} from "../services/localStorageService";
import UpdateTaskDialog from "../components/UpdateTaskDialog";
import ChangeTaskStatusDialog from "../components/ChangeTaskStatusDialog";

const MyTask = () => {
  const [isChangeTaskStatusDialogOpen, setIsChangeTaskStatusDialogOpen] = useState(false);
  const handleOpenChangeTaskStatusDialog = () => {
    handleCloseMenuAction();
    setIsChangeTaskStatusDialogOpen(true);
  };
  const handleCloseChangeTaskStatusDialog = () => {
    setIsChangeTaskStatusDialogOpen(false);
  };

  const [isUpdateTaskDialogOpen, setIsUpdateTaskDialogOpen] = useState(false);
  const handleOpenUpdateTaskDialog = () => {
    handleCloseMenuAction();
    setIsUpdateTaskDialogOpen(true);
  };
  const handleCloseUpdateTaskDialog = () => {
    setIsUpdateTaskDialogOpen(false);
  };

  const { showSnackbar } = useSnackbar();

  // const [selectedRowId, setSelectedRowId] = useState(null); // State cho selected row ID
  const [selectedRow, setSelectedRow] = useState(null); // State cho selected row (dữ liệu của row)
  const [anchorElRow, setAnchorElRow] = useState(null); // State cho menu hành động

  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [hasMore, setHasMore] = useState(false);

  const tableRef = useRef(null);
  const navigate = useNavigate();
  const loadInfo = useLoadUserInfo();

  const columns = [
    { id: "name", label: "Tên", filterable: false },
    { id: "status", label: "Trạng thái", filterable: true, render: (row) => row.status.name },
    { id: "assigned", label: "Phụ trách bởi", filterable: true,
      render: (row) => `${row.assignedUserProfile.fullName} (${row.assignedUserProfile.email})` },
    { id: "project", label: "Thuộc dự án", filterable: true,
      render: (row) => row.projectName },
    { id: "date", label: "Thời gian tạo", filterable: false, render: (row) => handleDate(row.createdAt) },
  ];

  useEffect(() => {
    loadInfo();
  }, []);

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate("/login");
    } else {
      loadData(page);
    }
  }, [navigate, page]);

  const loadData = (page) => {
    setLoading(true);
    getMyTasks(page)
      .then((response) => {
        setTotalPages(response.data.result.totalPages);
        setData((prevData) => [...prevData, ...response.data.result.data]);
        setHasMore(response.data.result.data.length > 0);
      })
      .catch((error) => {
        if (error.response?.status === 401) {
          logOut();
          navigate("/login");
        }
      })
      .finally(() => {
        setLoading(false);
      });

  };

  const getProjectName = (projectId) => {
    return getProjectById(projectId)
      .then((response) => response.data?.result?.name)
      .catch((error) => {
        if (error.response?.status === 401) {
          logOut();
          navigate("/login");
        }
      });
  };

  const handleScroll = () => {
    if (!hasMore) return;

    const tableContainer = tableRef.current;
    if (tableContainer) {
      const { scrollTop, scrollHeight, clientHeight } = tableContainer;
      if (scrollTop + clientHeight >= scrollHeight - 100) {
        if (page < totalPages) {
          setPage((prevPage) => prevPage + 1);
        }
        setHasMore(false);
      }
    }
  }

  const submitChangeTaskStatus = (data) => {
    changeTaskStatus(selectedRow.id, data)
      .then((response) => {
        showSnackbar("Cập thật thành công", "success");
        const task = response?.data?.result;

        setData((prevData) => prevData.map((row) => {
          if (row.id === task?.id) {
            return task;
          }
          return row;
        }));
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      })
      .finally(() => {
        handleCloseMenuAction();
      });
  };

  const submitUpdateTask = (data) => {
    updateTask(selectedRow.id, data)
      .then((response) => {
        showSnackbar("Cập thật thành công", "success");
        const task = response?.data?.result;

        setData((prevData) => prevData.map((row) => {
          if (row.id === task?.id) {
            return task;
          }
          return row;
        }));
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      })
      .finally(() => {
        handleCloseMenuAction();
      });
  };

  const handleDoubleClickRow = (e, row) => {
    navigate("/task/" + row?.id);
  }

  const handleClickActionRow = (e, row) => {
    setAnchorElRow(e.currentTarget); // Mở menu hành động
    setSelectedRow(row); // Cập nhật selectedRow khi click vào hành động
  };

  const handleCloseMenuAction = () => {
    setAnchorElRow(null); // Đóng menu
  };

  const handleUpdate = () => {
    alert(`Updating row: ${selectedRow.name}`);
    handleCloseMenuAction();
  };

  const handleRemove = () => {
    alert(`Removing row: ${selectedRow.name}`);
    handleCloseMenuAction();
  };

  return (
    <Scene>
      <Box sx={{display: "flex", justifyContent: "space-between", alignItems: "center", mb: 3}}>
        <TitleTypography>
          Công việc của tôi
        </TitleTypography>
      </Box>


      <ChangeTaskStatusDialog
        open={isChangeTaskStatusDialogOpen}
        onClose={handleCloseChangeTaskStatusDialog}
        onSubmit={submitChangeTaskStatus}
        currentStatus={selectedRow?.status?.name}
      />

      <UpdateTaskDialog
        open={isUpdateTaskDialogOpen}
        onClose={handleCloseUpdateTaskDialog}
        onSubmit={submitUpdateTask}
        oldData={{name: selectedRow?.name, description: selectedRow?.description}}
      />

      <ReusableTable
        columns={columns}
        data={data}
        // selectedRowId={selectedRowId}
        // onRowClick={handleClickRow}
        onDoubleClickRow={handleDoubleClickRow}
        onActionClick={handleClickActionRow}
        loading={loading}
        onScroll={handleScroll}
        ref={tableRef}
      />

      {/* Menu action for task */}
      <Menu
        anchorEl={anchorElRow}
        open={Boolean(anchorElRow)}
        onClose={handleCloseMenuAction}
        anchorOrigin={{vertical: "top", horizontal: "right"}}
        transformOrigin={{vertical: "top", horizontal: "right"}}
      >
        {selectedRow?.assignedUserProfile?.userId === getMyProfile()?.userId && (
          <CustomMenuItem onClick={handleOpenChangeTaskStatusDialog}>Cập nhật trạng thái</CustomMenuItem>
        )}
        {selectedRow?.assignedUserProfile?.userId === getMyProfile()?.userId && (
          <CustomMenuItem onClick={handleOpenUpdateTaskDialog}>Cập nhật</CustomMenuItem>
        )}
        {selectedRow?.createdAt &&
          new Date() - new Date(selectedRow.createdAt) < 3600000 && (
            <CustomMenuItem onClick={handleRemove}>Xóa công việc</CustomMenuItem>
          )
        }
      </Menu>
    </Scene>);
};


export default MyTask;
