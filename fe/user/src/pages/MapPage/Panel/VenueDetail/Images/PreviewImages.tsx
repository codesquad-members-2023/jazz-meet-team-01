import { css } from '@emotion/react';
import styled from '@emotion/styled';
import { IconButton } from '@mui/material';
import CaretLeft from '~/assets/icons/CaretLeft.svg?react';
import { VenueDetailData } from '~/types/api.types';

const PREVIEW_IMAGE_COUNT = 5;

type Props = {
  onReturnButtonClick: () => void;
  onImageClick: (index: number) => void;
} & Pick<VenueDetailData, 'images'>;

export const PreviewImages: React.FC<Props> = ({
  images,
  onReturnButtonClick,
  onImageClick,
}) => {
  const displayedImages = images.slice(0, PREVIEW_IMAGE_COUNT);
  const defaultImages = Array(5 - displayedImages.length).fill('default');

  return (
    <StyledImages>
      {displayedImages.map((image, index) => (
        <StyledImageWrapper key={image.id}>
          <StyledImage src={image.url} onClick={() => onImageClick(index)} />
          {index === PREVIEW_IMAGE_COUNT - 1 && (
            <StyledMoreImagesButton
              onClick={() => onImageClick(PREVIEW_IMAGE_COUNT)}
            >
              더보기
            </StyledMoreImagesButton>
          )}
        </StyledImageWrapper>
      ))}
      {defaultImages.map((text, index) => (
        <StyledImageWrapper key={`${text}-${index}`}>
          <StyledDefaultImage src={'/src/assets/icons/JazzMeet.svg'} />
          <StyledDefaultText>사진이 없어요</StyledDefaultText>
        </StyledImageWrapper>
      ))}
      <IconButton
        type="button"
        sx={{ position: 'absolute' }}
        onClick={onReturnButtonClick}
      >
        <CaretLeft />
      </IconButton>
    </StyledImages>
  );
};

const StyledImages = styled.div`
  position: relative;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: 4px;
`;

const StyledImageWrapper = styled.div`
  position: relative;
  width: 100%;
  padding-top: 100%;

  &:first-of-type {
    grid-column: 1 / 3;
    grid-row: 1 / 3;
  }
`;

const StyledImage = styled.img`
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  cursor: pointer;

  &:hover {
    opacity: 0.7;
  }
`;

const StyledDefaultImage = styled.img`
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: contain;
  background: #eeeeee;
  opacity: 0.3;
`;

const textOnImageStyle = css`
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const StyledDefaultText = styled.div`
  ${textOnImageStyle}
  font-size: 18px;
  font-weight: bold;
`;

const StyledMoreImagesButton = styled.button`
  ${textOnImageStyle}
  font-size: 28px;
  color: #fff;
  background: rgba(0, 0, 0, 0.3);
  cursor: pointer;
`;
