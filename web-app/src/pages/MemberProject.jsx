import React, {useEffect, useRef, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {Box, Button, Menu} from "@mui/material";
import {isAuthenticated, logOut} from "../services/authenticationService";
import Scene from "./Scene";
import IconButton from "@mui/material/IconButton";
import {styled} from "@mui/system";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import TitleTypography from "../components/TitleTypography";
import {addMember, getMemberInProject, getProjectById, removeMember} from "../services/projectService";
import ReusableTable from "../components/ReusableTable";
import PrevTitle from "../components/PrevTitle";
import ButtonAddTitle from "../components/ButtonAddTitle";
import CustomMenuItem from "../components/CustumMenuItem";
import {useSnackbar} from "../context/SnackbarContext";
import AddMemberDialog from "../components/AddMemberDialog";
import ConfirmDialog from "../components/ConfirmDialog";

const MemberProject = () => {
  const [confirmRemoveDialogOpen, setConfirmRemoveDialogOpen] = useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const handleOpenDialog = () => {
    setIsDialogOpen(true);
  };
  const handleCloseDialog = () => {
    setIsDialogOpen(false);
  };

  const { showSnackbar } = useSnackbar();
  const { projectId } = useParams(); // Lấy projectId từ URL

  const [selectedRow, setSelectedRow] = useState(null); // State cho selected row (dữ liệu của row)
  const [anchorElRow, setAnchorElRow] = useState(null); // State cho menu hành động


  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [hasMore, setHasMore] = useState(false);

  const tableRef = useRef(null);
  const navigate = useNavigate();
  const [project, setProject] = useState({});

  const columns = [
    { id: "name", label: "Họ tên", filterable: false, render: (row) => row.userProfile.fullName },
    { id: "username", label: "Username", filterable: false, render: (row) => row.userProfile.username },
    { id: "email", label: "Email", filterable: false, render: (row) => row.userProfile.email },
    { id: "role", label: "Vai trò", filterable: true, render: (row) => row.role.name },
  ];

  useEffect(() => {
    getProjectById(projectId)
      .then((response) => setProject(response?.data?.result))
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
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
    getMemberInProject(projectId, page)
      .then((response) => {
        // setTotalPages(response.data.result.totalPages);
        // setData((prevData) => [...prevData, ...response.data.result.data]);
        // setHasMore(response.data.result.data.length > 0);

        setData((prevData) => [...prevData, ...response.data.result]);
        setHasMore(false);
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
  }

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
  };

  const submitAddMember = (data) => {
    addMember(data).then((response) => {
      const member = response?.data?.result;
      setData((prevData) => [member, ...prevData]);
      showSnackbar("Đã thêm thành viên", "success");
    })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  }

  // const handleClickRow = (e, rowId) => {
  //   setSelectedRowId(rowId); // Cập nhật selectedRowId khi click vào dòng
  // };

  const handleDoubleClickRow = (e, row) => {
    navigate("/profile/" + row.userId);
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

  const handleRemoveMember = () => {
    removeMember(selectedRow.id)
      .then((response) => {
        showSnackbar("Đã xóa thành viên", "success");

        setData((prevData) => prevData.filter((row) => row.id !== selectedRow.id));
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  };

  // Hàm điều hướng khi click vào button
  const handleNavigate = (path) => {
    navigate(`/project/${projectId}/${path}`);
  };

  return (
    <Scene>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between", // Chia đều không gian giữa các thành phần
          alignItems: "center",
          mb: 3,
        }}
      >
        {/* Left Section: Title */}
        <Box sx={{ display: "flex", alignItems: "center" }}>
          <PrevTitle
            onClick={() => navigate(`/project/${projectId}`)} // Chuyển đến trang project
            sx={{ marginRight: 1 }} // Khoảng cách giữa button và typography
          >
            {project?.name}
          </PrevTitle>
          <IconButton
            sx={{
              padding: 0,
              marginRight: 1,
            }}
          >
            <ChevronRightIcon />
          </IconButton>
          <TitleTypography>
            Thành viên
          </TitleTypography>
        </Box>

        {/* Right Section: Add Member Button */}
        {project?.myRole?.name === "MANAGER" && (
          <ButtonAddTitle onClick={handleOpenDialog}>
            Thành viên mới
          </ButtonAddTitle>
        )}
      </Box>

      <AddMemberDialog
        open={isDialogOpen}
        onClose={handleCloseDialog}
        projectId={projectId}
        onSubmit={submitAddMember}
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
        showAction={project?.myRole?.name === "MANAGER"}
        sx={{
          height: "calc(100vh - 260px)",
        }}
      />

      {/* Menu for update and remove */}
      <Menu
        anchorEl={anchorElRow}
        open={Boolean(anchorElRow)}
        onClose={handleCloseMenuAction}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
        transformOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <CustomMenuItem
          onClick={() => {
            setConfirmRemoveDialogOpen(true);
            handleCloseMenuAction();
          }}
        >Xóa thành viên</CustomMenuItem>
      </Menu>

      <ConfirmDialog
        open={confirmRemoveDialogOpen}
        onClose={() => setConfirmRemoveDialogOpen(false)}
        onConfirm={() => {
          handleRemoveMember();
          setConfirmRemoveDialogOpen(false);
        }}
        title="Xác nhận"
        message="Bạn có chắc chắn muốn xóa thành viên này?"
      />
    </Scene>
  );
}

export default MemberProject;